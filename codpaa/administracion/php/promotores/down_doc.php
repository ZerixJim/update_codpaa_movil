<?
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	include_once('../../connexion/bdManager.php');
	include_once('../../php/seguridad.php');
	$manager = new bdManager();
	
	$file = $_REQUEST["file"];
	$sql_doc="select url,id_documento from promotores_expediente where id_expediente='".$file."'";
	$rs_doc=$manager->ejecutarConsulta($sql_doc);
	$dat_doc=mysqli_fetch_array($rs_doc);
	
	if($dat_doc['id_documento']!=10)
	{
		$doc_down='../'.$dat_doc['url'];
	}
	else
	{
		$doc_down=$dat_doc['url'];
		}

	if (file_exists($doc_down)) {
		header('Content-Description: File Transfer');
		header('Content-Type: application/octet-stream');
		header("Content-Type: application/force-download");
		header('Content-Disposition: attachment; filename=' . urlencode(basename($doc_down)));
		// header('Content-Transfer-Encoding: binary');
		header('Expires: 0');
		header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
		header('Pragma: public');
		header('Content-Length: ' . filesize($doc_down));
		ob_clean();
		flush();
		readfile($doc_down);
		exit;
	}
	else
	{
		echo 'no exite archivo';
		}
}
?>