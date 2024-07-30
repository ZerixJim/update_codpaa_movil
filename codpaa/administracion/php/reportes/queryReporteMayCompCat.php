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
		
 $manager = new bdManager();
///*********************************Recibo los datos de los filtros *******************************//


    $Formato = $_REQUEST['Formato'];
	
	$id_estado = $_REQUEST['idEstado'];
	 
	$MesLab = $_REQUEST['MesLab'];
	
	$GrupoM=$_REQUEST['GrupoM'];
	
	$regionmx=$_REQUEST['RegionMx'];
	  
	$fecha_c=explode('-',$MesLab);
	
	$filtro="";
	if($Formato!="")
	{
		$filtro.=" and mT.cadena='".$Formato."'";
		}
	
	if($id_estado!="" && $id_estado!="all")
	{
		$filtro.=" and mT.idEstado='".$id_estado."'";
		}
	if($regionmx!="" && $regionmx!="all")
	{
		
		$query_est="select * from estados where id_region='".$regionmx."'";
		$rs_est=$manager->ejecutarConsulta($query_est);
		$n_est=0;
	    while($dat_est=mysqli_fetch_array($rs_est))
		{
		 	if($n_est==0)
			{
				$filtro.=" and (mT.idEstado='".$dat_est['id']."'";
				}  		
			else
			{
				$filtro.=" or mT.idEstado='".$dat_est['id']."'";
				}
			$n_est++;
		}
		
		$filtro.=")";
	}
	if($GrupoM!="" && $GrupoM!="TODOS")
	{
		$filtro.=" and mT.grupo='".$GrupoM."'";
		}
	
   
	
		
	$sql_tienda="Select DISTINCT mT.idTienda from cod_tienda_marca_promotor ctm  
	inner join maestroTiendas mT on (mT.idTienda=ctm.idTienda)
	where mT.idTipoTienda='2' and mes='".$fecha_c[0]."' and anio='".$fecha_c[1]."' ".$filtro."
		GROUP BY ctm.idTienda
		order by ctm.idTienda ASC";
	
	   $rs_tienda=$manager->ejecutarConsulta($sql_tienda);
	   
	   	
		echo ' <div class="table-responsive-vertical shadow-z-1">
		<table class="table table-hover table-mc-light-green">
		<thead>
		<tr bgcolor="#FFA726">
		<th>No</th>  
		<th>Cadena</th>
		<th>Tienda</th>
		<th>Ciudad</th>
		<th>Formato</th>
		<th>Estado</th>';
		
		$sql_marcas="select cod.idMarca,m.nombre from cod_tienda_marca_promotor cod 
			inner join maestroTiendas mt on (mt.idTienda=cod.idTienda)
			INNER JOIN Marca m on (cod.idMarca=m.idMarca)
			where mt.idTipoTienda='2' and cod.idMarca<82 GROUP BY cod.idMarca";
		$rs_marcas=$manager->ejecutarConsulta($sql_marcas);
		
		while($dat_marcas=mysqli_fetch_array($rs_marcas))
		{
			echo '<th>'.$dat_marcas['nombre'].'</th>';
			}
		
		
		echo'</tr>
		</thead>
		';
		
	$tot_tien=0;
	///// **********************************Ciclo Tiendas
	while($dato_tienda=mysqli_fetch_array($rs_tienda))
	{
		
			$sql_ruta = "select mT.*,
			f.grupo AS formato,
			e.nombre AS estadoMex,
			l.nombre AS ciudadN
			from maestroTiendas mT 
			LEFT JOIN tiendas_formatos f ON (mT.idFormato = f.idFormato)
			LEFT JOIN estados e ON (e.id = mT.idEstado)
			LEFT JOIN localidades l on (mT.municipio=l.id)
			where idTienda='".$dato_tienda['idTienda']."'";
				
			$rs_ruta=$manager->ejecutarConsulta($sql_ruta);
			
			$dato_ruta=mysqli_fetch_array($rs_ruta);
			
			echo '
			<tr>
			<td data-title="No"><span>'.$dato_ruta['numeroEconomico'].'</span></td>
			<td data-title="Cadena"><span>'.$dato_ruta['cadena'].'</span></td>
			<td data-title="Tienda"><span>'.utf8_encode($dato_ruta['sucursal']).'</span></td>
			<td data-title="Ciudad"><span>'.utf8_encode($dato_ruta['ciudadN']).'</span></td>
			<td data-title="Formato"><span>'.$dato_ruta['formato'].'</span></td>
			<td data-title="Estado"><span>'.utf8_encode($dato_ruta['estadoMex']).'</span></td>
			';
			
			//******************************Catalogacion  de marcas **************//
			$sql_marcas="select cod.idMarca,m.nombre from cod_tienda_marca_promotor cod 
			inner join maestroTiendas mt on (mt.idTienda=cod.idTienda)
			INNER JOIN Marca m on (cod.idMarca=m.idMarca)
			where mt.idTipoTienda='2' and cod.idMarca<82 GROUP BY cod.idMarca";
		$rs_marcas=$manager->ejecutarConsulta($sql_marcas);
		
			while($dat_marcas=mysqli_fetch_array($rs_marcas))
			{
				$sql_cate="select count(idTienda) as tot_vis 
				from cod_tienda_marca_promotor 
				where idMarca='".$dat_marcas['idMarca']."' and mes='".$fecha_c[0]."' 
				and anio='".$fecha_c[1]."' and idTienda='".$dato_tienda['idTienda']."'";
				
				$rs_cate=$manager->ejecutarConsulta($sql_cate);
				$dat_cate=mysqli_fetch_array($rs_cate);
				
				if($dat_cate['tot_vis']>0)	
				{
				  echo '<td data-title="'.$dat_marcas['nombre'].'"><img src="../../images/marcas/'.$dat_marcas['idMarca'].'.gif" width="50px"/></td>';
				}
				else
				{
					echo '<td data-title="'.$dat_marcas['nombre'].'">---</td>';
					}
			}
						
				echo '</tr>';
		    usleep(1000);
			
			
	$tot_tien++;	
}
/////****************Final While Tiendas
  echo '<tr>
			<td colspan="6">Totales: '.$tot_tien.'</td>
			';
		$sql_marcas="select cod.idMarca,m.nombre from cod_tienda_marca_promotor cod 
			inner join maestroTiendas mt on (mt.idTienda=cod.idTienda)
			INNER JOIN Marca m on (cod.idMarca=m.idMarca)
			where mt.idTipoTienda='2' and cod.idMarca<82 GROUP BY cod.idMarca";
		$rs_marcas=$manager->ejecutarConsulta($sql_marcas);
		
			while($dat_marcas=mysqli_fetch_array($rs_marcas))
			{
				$sql_mar="select count(DISTINCT cod.idTienda) as tien_marc
				from cod_tienda_marca_promotor cod
				inner join maestroTiendas mT on (cod.idTienda=mT.idTienda)
				where idMarca='".$dat_marcas['idMarca']."' and mes='".$fecha_c[0]."' ".$filtro."
				and anio='".$fecha_c[1]."' and mT.idTipoTienda='2'";
				
				$rs_mar=$manager->ejecutarConsulta($sql_mar);
				$dat_mar=mysqli_fetch_array($rs_mar);
				
				echo '<td data-title="'.$dat_marcas['nombre'].'">'.$dat_mar['tien_marc'].'</td>';
				}
			
 echo '</tr>
 </table>
 </div>';// Cierra Tabla Principal

}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



