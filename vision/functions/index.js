const functions = require("firebase-functions");
const vision = require("@google-cloud/vision");

const client = new vision.ImageAnnotatorClient();

exports.annotateImage = functions.https.onCall(async (data, context) => {
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