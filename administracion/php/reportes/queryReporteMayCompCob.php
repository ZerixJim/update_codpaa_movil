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
	
   
	
		
	$sql_tienda="Select DISTINCT mT.idTienda,ctm.idPromotor from cod_tienda_marca_promotor ctm  
	inner join maestroTiendas mT on (mT.idTienda=ctm.idTienda)
	where mT.idTipoTienda='2' and mes='".$fecha_c[0]."' and anio='".$fecha_c[1]."' ".$filtro."
		GROUP BY ctm.idTienda
		order by ctm.idTienda ASC";
	
	   $rs_tienda=$manager->ejecutarConsulta($sql_tienda);
	   
	   	
		echo ' <div class="table-responsive-vertical shadow-z-1">
		<table class="table table-hover table-mc-light-green">
		<thead>
		<tr bgcolor="#FFA726">
		<th>Cartera</th>  
		<th>Regional</th>
		<th>Asesor</th>
		<th>Promotor</th>
		<th>Razon Social</th>
		<th>Sucursal</th>
		<th>Estado</th>
		<th>Municipio</th>
		<th>LU</th>
		<th>MA</th>
		<th>MI</th>
		<th>JU</th>
		<th>VI</th>
		<th>SA</th>
		</tr>
		</thead>
		';
		
	$tot_tien=0;
	///// **********************************Ciclo Tiendas
	while($dato_tienda=mysqli_fetch_array($rs_tienda))
	{
			$sql_prom="select rp.*,p.idCelular as Cartera,g.nombre as Regional,concat(s.nombreSupervisor,' ',s.apellidoSupervisor) as Asesor,p.nombre as Promotor 
			from rutasPromotores rp 		
			inner join Promotores p on (p.idCelular=rp.idPromotor)
			left join Supervisores s on (s.idSupervisores=p.Supervisor)
			left join Gerentes g on (g.idGerente=s.idGerente)
			where rp.idTienda='".$dato_tienda['idTienda']."' and rp.idPromotor='".$dato_tienda['idPromotor']."'
			";
			
			$rs_prom=$manager->ejecutarConsulta($sql_prom);
			$dato_prom=mysqli_fetch_array($rs_prom);
			
			
			$sql_ruta = "select mT.*,
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
			<td data-title="No"><span>'.$dato_prom['Cartera'].'</span></td>
			<td data-title="Cadena"><span>'.utf8_encode(trim($dato_prom['Regional'])).'</span></td>
			<td data-title="Tienda"><span>'.utf8_encode(trim($dato_prom['Asesor'])).'</span></td>
			<td data-title="Ciudad"><span>'.utf8_encode($dato_prom['Promotor']).'</span></td>
			<td data-title="Ciudad"><span>'.utf8_encode($dato_ruta['grupo']).'</span></td>
			<td data-title="Ciudad"><span>'.utf8_encode($dato_ruta['sucursal']).'</span></td>
			<td data-title="Formato"><span>'.htmlentities($dato_ruta['estadoMex']).'</span></td>
			<td data-title="Estado"><span>'.utf8_encode($dato_ruta['ciudadN']).'</span></td>
			<td data-title="No"><span>'.$dato_prom['lunes'].'</span></td>
			<td data-title="Cadena"><span>'.$dato_prom['martes'].'</span></td>
			<td data-title="Tienda"><span>'.$dato_prom['miercoles'].'</span></td>
			<td data-title="Ciudad"><span>'.$dato_prom['jueves'].'</span></td>
			<td data-title="Formato"><span>'.$dato_prom['viernes'].'</span></td>
			<td data-title="Estado"><span>'.$dato_prom['sabado'].'</span></td>
			';
			
					
				echo '</tr>';
		    usleep(1000);
			
			
	$tot_tien++;	
}
/////****************Final While Tiendas
 
 echo '</table>
 </div>';// Cierra Tabla Principal

}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



