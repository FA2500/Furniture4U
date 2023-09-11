<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\payController;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

//Route::get('/',[PageController::class,'index'])->name('front.index');

Route::get('/', function () {
    return view('welcome');
})->name('welcome');

Route::get('/pay', [payController::class, 'index'])->name('pay');
Route::post('/pay', [payController::class, 'callback'])->name('paycb');
Route::get('/receipt', [payController::class, 'getBill'])->name('receipt');
Route::get('/invoice', [payController::class, 'invoice'])->name('invoice');
Route::get('/invoiceview', [payController::class, 'viewInvoice'])->name('vinvoice');
Route::get('/paycheck', [payController::class, 'paycheck'])->name('paycheck');
Route::get('/success', [payController::class, 'succeed'])->name('succeed');
Route::get('/failed', [payController::class, 'failed'])->name('failed');