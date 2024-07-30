<?
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	include_once('../../connexion/bdManager.php');
	include_once('../../php/seguridad.php');
	$manager = new bdManager();
	
	$file = $_REQUEST["file"];
	$sql_doc="select pe.url,ed.documento from promotores_expediente pe 
	inner join expediente_docs ed on (pe.id_documento=ed.id_documento)
	where id_expediente='".$file."'";
	$rs_doc=$manager->ejecutarConsulta($sql_doc);
	$dat_doc=mysqli_fetch_array($rs_doc);
	
	if (file_exists('../'.$dat_doc['url'])) {
		$file = '../'.$dat_doc['url'];
		$filename = utf8_encode($dat_doc['documento']).'.pdf'; /* Note: Always use .pdf at the end. */
		
		header('Content-type: application/pdf');
		header('Content-Disposition: inline; filename="' . $filename . '"');
		header('Content-Transfer-Encoding: binary');
		header('Content-Length: ' . filesize($file));
		header('Accept-Ranges: bytes');
		
		@readfile($file);
	}
}
?>