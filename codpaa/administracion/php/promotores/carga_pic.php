<?php
error_reporting(E_ALL);
set_time_limit(0);

include_once('../../php/seguridad.php');
if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){


include_once('../../connexion/bdManager.php');

$manager = new bdManager();  

if ($_FILES['pic_up']["error"] > 0)
  {

	 echo $_FILES['pic_up']['error'];
	 echo  0;

  }
  else
  {
	    $idDoc=$_REQUEST['idDoc'];
		$idProm=$_REQUEST['idProm'];
		$sql_prom="select * from Promotores where idCelular='".$idProm."'";
		$rs_prom=$manager->ejecutarConsulta($sql_prom);
		$dat_prom=mysqli_fetch_array($rs_prom);
		
		
		$inputFileName="../../images/promoPic/".$dat_prom['idCelular']."/";
		
		if(!file_exists($inputFileName))
		{
			mkdir($inputFileName);
			}
		
		$inputFileName.=$_FILES['pic_up']['name'];
	  
	  	if(move_uploaded_file($_FILES['pic_up']['tmp_name'],$inputFileName))
		{	  
			$sql_doc="insert into promotores_expediente (idPromotor,id_documento,fecha,idUsuario,url,estatus) 
			values ('".$idProm."','".$idDoc."',now(),'".$_SESSION['idUser']."','".$inputFileName."','1')";
			
			if($manager->ejecutarConsulta($sql_doc))
			{
				echo 1;
				}
			else
			{
				echo 2;
				}
		}
		else
		{
			echo 0;
			}
	  }


}
?>