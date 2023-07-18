/* eslint-disable */
const admin = require('firebase-admin');
var serviceAccount = require("./serviceAccountKey.json");
const functions = require("firebase-functions");
const vision = require("@google-cloud/vision");
const Billplz = require('billplz');
const url = require('url');
const axios = require('axios');
const https = require('https');
const { resolve } = require('path');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const billplz = new Billplz({
  'key': 'bd3b9b73-7ae2-4c9c-8a37-b2382854556d',
  'sandbox': true
});

const client = new vision.ImageAnnotatorClient();

exports.annotateImage = functions.region('asia-southeast1').https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError(
      "unauthenticated",
      "annotateImage must be called while authenticated."
    );
  }
  try {
    return await client.annotateImage(data);
  } catch (e) {
    throw new functions.https.HttpsError("internal", e.message, e.details);
  }
});

exports.createBill = functions.region('asia-southeast1').https.onCall(async (data, context) => {

  const requestBody = JSON.stringify({
    collection_id: data.collection_id,
    description: data.description,
    email: data.email,
    name: data.name,
    amount: data.amount,
    callback_url: 'https://asia-southeast1-furniture4u-93724.cloudfunctions.net/callback',
    redirect_url: 'https://asia-southeast1-furniture4u-93724.cloudfunctions.net/redirect'
  });

  const config = {
    hostname: 'www.billplz-sandbox.com',
    path: '/api/v3/bills',
    method: 'POST',
    headers: {
      'Authorization': 'Basic YmQzYjliNzMtN2FlMi00YzljLThhMzctYjIzODI4NTQ1NTZk',
      'Content-Type': 'application/json',
      'Content-Length': requestBody.length,
    }
  };

  return new Promise((resolve, reject) => {
    const request = https.request(config, (response) => {
      let data = '';

      response.on('data', (chunk) => {
        data += chunk;
      });

      response.on('end', () => {
        const responseData = JSON.parse(data);
        resolve(responseData);
      });
    });

    request.on('error', (error) => {
      reject(error);
    });

    request.write(requestBody);
    request.end();
  });
});

exports.callback = functions.region('asia-southeast1').https.onRequest(async (req, res) => {
  const billplzId = req.body.id;
  const isPaid = req.body.paid;

  try {
    // perform any necessary operations here, such as updating the database
    res.status(200).send("Callback successful.");
  } catch (error) {
    console.error(error);
    res.status(500).send("Error processing callback.");
  }
});

exports.redirect = functions.region('asia-southeast1').https.onRequest(async (req, res) => {
    const queryObject = url.parse(req.url, true).query;
    const billplzId = queryObject['billplz[id]'];

    const config = {
      headers: {
        'Authorization': 'Basic YmQzYjliNzMtN2FlMi00YzljLThhMzctYjIzODI4NTQ1NTZk',
      }
    };

    const response = await axios.get('https://www.billplz-sandbox.com/api/v3/bills/'+queryObject['billplz[id]'], config);

    console.log(response.data.paid);

    // Check if the bill has been paid
    if (response.data.paid) {
      // Redirect to the specified URL
      console.log('Success');
      return res.redirect('https://asia-southeast1-furniture4u-93724.cloudfunctions.net/payment_success');
    } else {
      // Return the bill information
      console.log('Failed');
      return res.json('https://asia-southeast1-furniture4u-93724.cloudfunctions.net/payment_failed');
    }

    
  });


    //res.redirect('https://us-central1-furniture4u-93724.cloudfunctions.net/payment_success');


   /* console.log(obj['id']);
    console.log(obj['paid']);
    console.log(obj['x_signature']);*/
  /*try {
    res.redirect('https://us-central1-furniture4u-93724.cloudfunctions.net/payment_success');
  } catch (error) {
    // Error occurred, redirect to error page
    res.redirect('https://us-central1-furniture4u-93724.cloudfunctions.net/payment_failed');
  }*/


exports.payment_success = functions.region('asia-southeast1').https.onRequest(async (req, res) => {
  res.send('payment_success');
});

exports.payment_failed = functions.region('asia-southeast1').https.onRequest(async (req, res) => {
  res.send('payment_failed');
});

exports.notification = functions.region('asia-southeast1').https.onRequest(async (req, res) => {
  const {title,body,token} = req.body;
  const registrationToken = 'ck1L5-rjSE2bSL3cAyLJ3v:APA91bEgYneawEs1uAH3Cxu9UR8H6CR-AtwHjINdmegICJcAi_5QQWd8vK8Hsm2Nm4HgIIj2qsWBUNENhiWfeXCZGFdT4_pJYCIo1kod1sMax0RgDQtTk51tEE0WojrVWuF-xTmyuNFw';
  const message = {
    notification: {
      title: "TITLE",
      body: "BODY",
    },
    token: registrationToken,
  };

  return new Promise((res,rej)=>{
    admin
    .messaging()
    .sendEachForMulticast(message)
    .then((res)=> {
      console.log(res.responses);
      resolve(res);
    })
    .catch((error)=>{
      console.log(JSON.stringify(error));
      reject(error);
    });
  });
});
