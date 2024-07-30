<?
ob_start();
session_start();

include_once('../../../connexion/bdManager.php');
include_once('../../../php/seguridad.php');


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	$idSem = $_REQUEST['idSemana'];
	
	$manager = new bdManager();

	$sql_sem="Select semana,anio,idPromotor from supervisionRutas where idSupervision='".$idSem."'";
	$res_sem=$manager->ejecutarConsulta($sql_sem);
	$dat_sem=mysqli_fetch_array($res_sem);
		
	
	$sql_ruta="select mt.*,sp.idTienda,sum(lunes+martes+miercoles+jueves+viernes+sabado+domingo) as vis_req
	from supervisionRutas sp 
	inner join maestroTiendas mt on (mt.idTienda=sp.idTienda) 
	where semana='".$dat_sem['semana']."' and anio='".$dat_sem['anio']."' and idPromotor='".$dat_sem['idPromotor']."'
	group by sp.idTienda";
	
	$rs_ruta=$manager->ejecutarConsulta($sql_ruta);
	
	$result = array();  
	
	$dias_semana=array();
	
	//***** calculamos las fechas de la semana
	$dias_semana=$manager->diasSemana($dat_sem['anio'],$dat_sem['semana']);
	
	
	$visitas_tot=0;
	$requeridas_tot=0;
	//************* Recorremos la ruta de la semana elegida
    while($dat_ruta=mysqli_fetch_array($rs_ruta))
	{  
		
		//***********Recorre la semana para calcular las visitas en la tienda
		for($i=0;$i<=6;$i++)
		{
			$sql_vis="Select count(idTiendasVisitadas) as total_vis,latitud,longitud
			from tiendasVisitadas 
			where fecha='".$dias_semana[$i]."' and tipo='E' 
			and idTienda='".$dat_ruta['idTienda']."' and idCelular='".$dat_sem['idPromotor']."'";
			
			$rs_vis=$manager->ejecutarConsulta($sql_vis);
			
			$dat_vis=mysqli_fetch_array($rs_vis);
			
			if($dat_vis['latitud']!="")
			{
			   $distancia=$manager->Distancia($dat_ruta['x'],$dat_ruta['y'],$dat_vis['latitud'],$dat_vis['longitud'],'K');
			   
			   if($distancia<1)
			   {
				   $visitas_tot++;
				   }
			}
		
		
		}
		
		$requeridas_tot+=$dat_ruta['vis_req'];
		
   }
  
	$porcentaje_vis=round((($visitas_tot*100)/$requeridas_tot),2);
   
   
   
    echo $porcentaje_vis; 
	
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>