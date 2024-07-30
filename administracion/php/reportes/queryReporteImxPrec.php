<?php
/**

 * Created by DreamW.

 * User: Christian

 * Date: 11/12/14	

 * Time: 13:59

 */
ob_start();

session_start();

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
		

///*********************************Recibo los datos de los filtros *******************************//

	$manager = new bdManager();

	$id_estado = $_REQUEST['idEstado'];
	
	$MesLab = $_REQUEST['MesLab'];
		
	$fecha_c=explode('-',$MesLab);
	
	$dias_mes=date('t',$fecha_c[0]);
	
		
	$mes=intval($fecha_c[0]);

	 if($mes<10)
	 {
		 $mes="0".$mes;
		 }
	//Fechas del mes para consultas....
	$fecha_ini="01-".$mes."-".$fecha_c[1];
	
	$fecha_fin=$dias_mes."-".$mes."-".$fecha_c[1];
	
	
	
	$mes_vent=intval($fecha_c[0])-1;
	
	$dias_mes_v=date('t',$mes_vent);
	
	if($mes_vent<10)
	{
		$mes_vent="0".$mes_vent;
	}
	if($fecha_c[0]==1)
	{
		$anio_vent=$fecha_c[1]-1;
	}
	else
	{
		$anio_vent=$fecha_c[1];	
		}
	//Fechas del mes para consulta de ventas...
	$fecha_ini_v="01-".$mes_vent."-".$anio_vent;
	$fecha_fin_v=$dias_mes_v."-".$mes_vent."-".$anio_vent;
	
	$id_marca=$_REQUEST['idMarca'];
	
	$filtro="";
		
	if($id_estado!="" && $id_estado!="all")
	{
		$filtro.=" and mT.idEstado='".$id_estado."'";
		}
	
	
	$filtro_mar="";
	$sql_tienda="Select DISTINCT mT.idTienda from cod_tienda_marca_promotor ctm  
	inner join maestroTiendas mT on (mT.idTienda=ctm.idTienda)
	where mT.idTipoTienda='2' and mes='".$fecha_c[0]."' and anio='".$fecha_c[1]."' and idMarca='".$id_marca."'";
	$sql_tienda.=" ".$filtro."  
		GROUP BY ctm.idTienda
		order by mT.grupo ASC";
	/*$n_marc=count($id_marca);
		$k=0;
			if($n_marc>0)
			{
					foreach($id_marca as $marcas)
					{
					    if($k==0)
					  {
						$sql_tienda.=" '".$marcas."' ";
						$filtro_mar.=" '".$marcas."'";
					  }
					else
					{
						$sql_tienda.=" ,'".$marcas."'";
						$filtro_mar.= " ,'".$marcas."'";
						}
						$k++;
					}
					$sql_tienda.=")";
			}*/
		
	
	   $rs_tienda=$manager->ejecutarConsulta($sql_tienda);
	   
	   	
		echo ' <div class="table-responsive-vertical shadow-z-1">
		<table class="table table-hover table-mc-light-green scroll">
		<thead>
		<tr bgcolor="#FFA726">
		<th>No Econ</th>  
		<th>ID</th>
		<th>Cadena</th>
		<th>Grupo</th>
		<th>Tienda</th>
		<th>Ciudad</th>
		<th>Estado</th>';
		
		$query_prod="select concat(SUBSTRING(pro.nombre,1,20),' ',pro.presentacion) as nombre 
			from Producto pro 
			inner join inteligenciaMercado im on (im.idProducto=pro.idProducto)
			where pro.idMarca ='".$id_marca."' and (STR_TO_DATE(im.fecha,'%d-%m-%Y') 
			BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
			and pro.estatus='1' 
			Group by pro.idProducto order by nombre";
		
		$rs_prod=$manager->ejecutarConsulta($query_prod);
		
		while($dat_prod=mysqli_fetch_array($rs_prod))
		{
			echo "<th>".$dat_prod['nombre']."</th>";
			}
		
		
		echo '</tr>
		</thead>
		';
		
	
	///// **********************************Ciclo Tiendas
	while($dato_tienda=mysqli_fetch_array($rs_tienda))
	{
		
			$sql_ruta = "select mT.*,
			f.cadena AS cadenaf,
			e.nombre AS estadoMex,
			l.nombre AS ciudadN
			from maestroTiendas mT 
			LEFT JOIN tiendas_formatos f ON (mT.idFormato = f.idFormato)
			LEFT JOIN estados e ON (e.id = mT.idEstado)
			LEFT JOIN localidades l on (mT.id_municipio=l.id)
			where idTienda='".$dato_tienda['idTienda']."'";
				
			$rs_ruta=$manager->ejecutarConsulta($sql_ruta);
			
			$dato_ruta=mysqli_fetch_array($rs_ruta);
			
			$sql_vis="select COUNT(idTiendasVisitadas) as tot_vis from tiendasVisitadas 
			where fecha like '%".$fecha_c[0]."-".$fecha_c[1]."%' and idTienda='".$dato_tienda['idTienda']."' and tipo='E'";
			
			$rs_vis=$manager->ejecutarConsulta($sql_vis);
			
			$dato_vis=mysqli_fetch_array($rs_vis);
			
			if($dato_vis['tot_vis']>0)
			{
				$visitada="bgcolor='#58FA58'";
				}
			else
			{
				$visitada="bgcolor='#FE2E2E'";
				}
				
			echo '
			<tr>
			<td '.$visitada.' data-title="No"><span>'.$dato_ruta['numeroEconomico'].'</span></td>
			<td data-title="ID"><span>'.$dato_ruta['idTienda'].'</span></td>
			<td data-title="Cadena"><span>'.$dato_ruta['cadenaf'].'</span></td>
			<td data-title="Grupo"><span>'.$dato_ruta['grupo'].'</span></td>
			<td data-title="Tienda"><span>'.utf8_encode($dato_ruta['sucursal']).'</span></td>
			<td data-title="Ciudad"><span>'.utf8_encode($dato_ruta['ciudadN']).'</span></td>
			<td data-title="Estado"><span>'.utf8_encode($dato_ruta['estadoMex']).'</span></td>
			';
			
			$query_prod="select pro.idProducto from Producto pro
				inner join inteligenciaMercado im on (im.idProducto=pro.idProducto) 
				where pro.idMarca='".$id_marca."' and pro.estatus='1'
				and (STR_TO_DATE(im.fecha,'%d-%m-%Y') 
				BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
				Group by pro.idProducto
				order by nombre";
		
			$rs_prod=$manager->ejecutarConsulta($query_prod);
			
			while($dat_prod=mysqli_fetch_array($rs_prod))
			{
				
				$query_im_prod="select precioNormal from inteligenciaMercado where idTienda='".$dato_ruta['idTienda']."' 
				and idProducto='".$dat_prod['idProducto']."' and (STR_TO_DATE(fecha,'%d-%m-%Y') 
				BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))";
				
				$rs_im_prod=$manager->ejecutarConsulta($query_im_prod);
				
				echo "<td>";
				while($dat_im_prod=mysqli_fetch_array($rs_im_prod))
				{
					echo $dat_im_prod['precioNormal']." <br>";
					
					}
				
				echo "</td>";
			}
			
			
			echo '</tr>';	
		usleep(2500);
	
}
/////****************Final While Tiendas
  
 echo '</table>
 </div>';// Cierra Tabla Principal
?>

<?
}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



