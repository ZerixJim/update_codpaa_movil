<?php
error_reporting(E_ALL);
set_time_limit(0);

include_once('../php/seguridad.php');
if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){


include_once('../connexion/bdManager.php');


// ************************ Funcion para encriptar
function pdfEncrypt ($origFile, $password, $destFile){
//include the FPDI protection http://www.setasign.de/products/pdf-php-solutions/fpdi-protection-128/
require_once('fpdi/FPDI_Protection.php');

$pdf = new FPDI_Protection('P', 'in', 'letter');
// set the format of the destinaton file, in our case 6×9 inch
//$pdf->FPDF();
//calculate the number of pages from the original document
$pagecount = $pdf->setSourceFile($origFile);

// copy all pages from the old unprotected pdf in the new one
for ($loop = 1; $loop <= $pagecount; $loop++) {
    $tplidx = $pdf->importPage($loop);
    $pdf->addPage();
    $pdf->useTemplate($tplidx);
}

// protect the new pdf file, and allow no printing, copy etc and leave only reading allowed

$pdf->SetProtection(array('print'),$password);
$pdf->Output($destFile, 'F');

return $destFile;
} // ****************Final de la Funcion

$manager = new bdManager();  

if ($_FILES['doc_up']["error"] > 0)
  {

	 echo $_FILES['doc_up']['error'];
	 echo  0;

  }
  else
  {
	  $inputFileName=$_FILES['doc_up']['tmp_name'];
	  
	  //move_uploaded_file($_FILES['file_rutas']['tmp_name'],"PHPExcel/" . $_FILES['file_rutas']['name']);
	  
	  $idDoc=$_REQUEST['idDoc'];
		$idProm=$_REQUEST['idProm'];
		$sql_prom="select * from Promotores where idCelular='".$idProm."'";
		$rs_prom=$manager->ejecutarConsulta($sql_prom);
		$dat_prom=mysqli_fetch_array($rs_prom);
		
		
		
		//password for the pdf file
		$password = $dat_prom['usuario'].$dat_prom['idCelular'];
		
		//name of the original file (unprotected)
		$origFile = $inputFileName;
		
		//name of the destination file (password protected and printing rights removed)
		$destFile ='../../../promoFiles/'.$dat_prom['idCelular'].'/'.$idDoc.' - '.$dat_prom['idCelular'].'.pdf';
		
		if(!file_exists('../../../promoFiles/'.$dat_prom['idCelular'].'/'))
		{
			mkdir('../../../promoFiles/'.$dat_prom['idCelular'].'/');
			}
		
		//encrypt the book and create the protected file
		pdfEncrypt($origFile, $password, $destFile );
		
		
		$sql_doc="insert into promotores_expediente (idPromotor,id_documento,fecha,idUsuario,url,estatus) 
		values ('".$idProm."','".$idDoc."',now(),'".$_SESSION['idUser']."','".$destFile."','1')";
		
		if($manager->ejecutarConsulta($sql_doc))
		{
			echo 1;
			}
		else
		{
			echo 0;
			}

	  }




}
?>