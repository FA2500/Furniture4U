<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Billplz\Laravel\Billplz;
use Billplz\Client;
use Barryvdh\Debugbar\Facades\Debugbar;

use PDF;
use Dompdf\Dompdf;


class payController extends Controller
{
    public function index(Request $request)
    {
        $billplz = Client::make(config('services.billplz.key'),config('services.billplz.x-signature'));
        $billplz->useSandbox();
        
        $bill = $billplz->bill();

        $response = $bill->create(
            'hr10bp8q',
            $request->header("email"),
            $request->header("phone"),
            $request->header("name"),
            floatval($request->header("amount")),
            route("paycb"), 
            $request->header("desc"),
            ["redirect_url" => route("paycheck") ] ,
         )->toArray();

         /*$response = $bill->create(
            "hr10bp8q",
            "mfarisammar@gmail.com",
            "01156403489",
            $request->header("name"),
            550,
            route("paycb"), 
            "Hotel Booking",
            ["redirect_url" => route("welcome") ],
         )->toArray();*/

         //var_dump($bill);
         return redirect($response['url']); 
    }

    public function callback(Request $request)
    {
        //info($request);
        //$data = $request->except(['x_signature']);
        //$signature = new Signature(config('services.billplz.x-signature'), Signature::WEBHOOK_PARAMETERS);

        //if ($signature->verify($data, $request->x_signature)) {
            //$order = Order::where('invoice_number', $request->id)->first();
            if ($request->paid == 'true') 
            {
                return response()->json(['success', true], 200);
            }
            else if ($request->paid == 'false')
            {
                return response()->json(['success', false], 200);
            }
        //} 
        else {
            abort(403);
        }
    }

    public function getBill(Request $request)
    {
        $billplz = Client::make(config('services.billplz.key'),config('services.billplz.x-signature'));
        $billplz->useSandbox();
        $bill = $billplz->bill();

        $test = $request->toArray();;
        $response = $bill->get($test['billplz']['id'])->toArray();    

        return redirect($response['url']); 
    }

    public function invoice()
    {
        /*$pdf_doc = PDF::loadView('invoice');
        $pdf_doc->download('pdf.pdf');
        return view('invoice');*/
        $pdf_doc = PDF::loadView('invoice2');
        $pdf_doc->download('pdf.pdf');
        $this->invoices();
        return view('invoice2');
    }

    public function invoices()
    {
        $pdf_doc = PDF::loadView('invoice2');
        return $pdf_doc->download('invoice.pdf');
    }

    public function viewInvoice()
    {
        return view('invoice2');
    }
    
    public function paycheck(Request $request)
    {
        print("TEST");
        print($request->billplz['paid']);

       /* if ($request->billplz['paid'] == true) 
            {
                return redirect(route("failed")); 
            }
            else if ($request->billplz['paid'] == false) 
            {
                return redirect(route("failed")); 
            }*/
            if ($request->billplz['paid'] == "true") 
            {
                return redirect(route("succeed")); 
            }
            else if ($request->billplz['paid'] == "false") 
            {
                return redirect(route("failed")); 
            }
            else
            {
                return redirect(route("failed")); 
            }
    }
    
    public function succeed()
    {
        print("success");
        return;
    
    }
    
    public function failed()
    {
        print("failed");
        return;
    }
}
