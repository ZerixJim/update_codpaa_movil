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


    $id_formato = $_REQUEST['idFormato'];
	
	$id_estado = $_REQUEST['idEstado'];
	 
	$desde = $_REQUEST['Desde'];
	  
	$hasta = $_REQUEST['Hasta'];
	
	$id_marca=$_REQUEST['idMarca'];
	
	$id_tipoT=$_REQUEST['idTipoTie'];
	
	$filtro="";
	if($id_formato!="")
	{
		$filtro.=" and mT.idFormato='".$id_formato."'";
		}
	
	if($id_estado!="" && $id_estado!="all")
	{
		$filtro.=" and mT.idEstado='".$id_estado."'";
		}
	if($id_tipoT!="")
	{
		$filtro.=" and mT.idTipoTienda='".$id_tipoT."'";
		}

    $manager = new bdManager();
	
	//******************************************************************************************//
	$query_marca="select nombre from Marca where idMarca='".$id_marca."'";
	$resul_marca=$manager->ejecutarConsulta($query_marca);
	$datos_marca=mysqli_fetch_array($resul_marca);
	
	//***********Si se tiene el logo de la marca ******//
	if(file_exists('../../imagenes/logos/'.$datos_marca['nombre'].'.png'))
	{
		echo '<img src="../../imagenes/logos/'.$datos_marca['nombre'].'.png" height="200px" />';
	}
		
		$sql_tienda="Select DISTINCT mT.idTienda from supervisionRutas r  
		inner join maestroTiendas mT on (mT.idTienda=r.idTienda)
		where idPromotor IN (select idCelular from Promotores p inner join marcaAsignadaPromotor mp 
		on (p.idCelular=mp.idPromotor) 
		where p.status='a' ";
		
		$n_marc=count($id_marca);
		$k=0;
			if($n_marc>0)
			{
					foreach($id_marca as $marcas)
					{
					    if($k==0)
					  {
						$sql_tienda.=" and (mp.idMarca='".$marcas."' ";
					  }
					else
					{
						$sql_tienda.=" or mp.idMarca='".$marcas."'";
						}
						$k++;
					}
					$sql_tienda.=")";
			}
		$sql_tienda.=" ".$filtro."  
		GROUP BY p.idCelular)
		GROUP BY r.idTienda,r.idPromotor
		order by r.idTienda ASC";
	
	   $rs_tienda=$manager->ejecutarConsulta($sql_tienda);
	   
	   usleep(3000);
	
		echo '<table width="100%" border="1" bordercolor="#D8D8D8">
		<tr bgcolor="#FF8000">
		<td><span><strong>ID</strong></span></td>
		<td><span><strong>CADENA</strong></span></td>
		<td><span><strong>No.</strong></span></td>
		<td><span><strong>TIENDA</strong></span></td>
		<td><span><strong>CIUDAD</strong></span></td>
		<td><span><strong>FORMATO</strong></span></td>
		<td><span><strong>ESTADO</strong></span></td>
		<td><span><strong>ISLAS</strong></span></td>
		<td><span><strong>FRENTES</strong></span></td>
		<td><span><strong>VISITAS X SEM REQ.</strong></span></td>
		<td><span><strong>VISITAS MINIMAS</strong></span></td>
		';
		
		///***************Recorrer los dias laborados **/////	
		$desde2=$desde;
		while(strtotime($desde2)<=strtotime($hasta)) { 
		
		
			$fecha=explode('-',$desde2);
			$n_dia=$manager->diaSemana($fecha[0],$fecha[1],$fecha[2]);
			
			if($n_dia<=6 || $n_dia>=1)
			{
			echo '<td align="center"><span>'.$desde2.'</span>
				</td>';
			}
			
			$desde2 = date("d-m-Y", strtotime( "$desde2 + 1 DAY")) ; 
		} 
			
		echo '
		<td align="center"><span><strong>FOTOS</strong></span></td>
		<td width="10px"><span>CUMPLE</span></td>
		<td><span><strong>TOTAL</strong></span></td>
		</tr>';
	
	///// **********************************Ciclo Tiendas
	while($dato_tienda=mysqli_fetch_array($rs_tienda))
	{
			
		//****Verifica si tiene visitas en el rango de fecha
		
		$sql_ver="SELECT count(idTiendasVisitadas) as total_vis
			FROM tiendasVisitadas
			WHERE idTienda = '".$dato_tienda['idTienda']."'
			AND (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') 
			and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) and tipo='E'";
			
		$rs_ver=$manager->ejecutarConsulta($sql_ver);
		$dat_ver=mysqli_fetch_array($rs_ver);	
		
		if($dat_ver['total_vis']>0)
		{
			
			$sql_ruta = "select mT.*,
			f.grupo AS formato,
			f.cadena AS cadenaf,
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
			<td><span>'.$dato_ruta['idTienda'].'</span></td>
			<td><span>'.$dato_ruta['cadenaf'].'</span></td>
			<td><span>'.$dato_ruta['numeroEconomico'].'</span></td>
			<td><span>'.utf8_encode($dato_ruta['sucursal']).'</span></td>
			<td><span>'.utf8_encode($dato_ruta['ciudadN']).'</span></td>
			<td><span>'.$dato_ruta['formato'].'</span></td>
			<td><span>'.utf8_encode($dato_ruta['estadoMex']).'</span></td>
			';
			
			//******************************Cuenta de Islas **************//
			$n_marc=count($id_marca);
			$islas_tot=0;
			$frentes_tot=0;
				if($n_marc>0)
				{
					foreach($id_marca as $marcas)
					{
					   $query_isla_fr="CALL tiendas_vis(".$dato_tienda['idTienda'].",".$marcas.",'".$desde."','".$hasta."');";
			
						$rs_isla_fr=$manager->ejecutarConsulta($query_isla_fr);
						
						$dat_isla_fr=mysqli_fetch_array($rs_isla_fr);
						
						
						if($dat_isla_fr['islas']!=NULL || $dat_isla_fr['islas']!="")
						{
							$islas_tot+=$dat_isla_fr['islas'];
						}
						if($dat_isla_fr['frentesTot']!=NULL || $dat_isla_fr['frentesTot']!="")
						{
							$frentes_tot+=$dat_isla_fr['frentesTot'];
							}
						
					}
						
				}
			
			
			if($islas_tot!=0)
			{
				echo '<td><span>'.$islas_tot.'</span></td>';
			}
			else
			{
				echo '<td><span>---</span></td>';
				}
			
			/*$query_isla="select count(idphotoCatalogo) as islas from photoCatalogo where id_exhibicion='1'
			and (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') )
			and id_tienda='".$dato_tienda['idTienda']."' and id_marca='".$id_marca."'";
			
			$rs_isla=$manager->ejecutarConsulta($query_isla);
			
			$dat_isla=mysqli_fetch_array($rs_isla);
			
			if($dat_isla['islas']!=NULL || $dat_isla['islas']!="")
			{
				echo '<td><span>'.$dat_isla['islas'].'</span></td>';
			}
			else
			{
				echo '<td><span>---</span></td>';
				}
			
			
			$query_frentes="select sum(cha1+cha2+cha3+cha4+cha5+cha6) as frentes_tot from frentesCharola 
			where (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
					and idTienda='".$dato_tienda['idTienda']."' and idMarca='".$id_marca."'";
			
			$rs_frentes=$manager->ejecutarConsulta($query_frentes);
			
			$dat_frentes=mysqli_fetch_array($rs_frentes);
			*/
			
			//********************************* Sumar Frentes ********************************
			if($frentes_tot!=0)
			{
				echo '<td><span>'.$frentes_tot.'</span></td>';
			}
			else
			{
				echo '<td><span>---</span></td>';
				}
			
			///******************Visitas Requeridas dependiendo del tipo de tienda *****//
			
			if($dato_ruta['idTipoTienda']==2)
			{
				echo '<td><span>3</span></td>';
				echo '<td><span>3</span></td>';
				}
			else 
			{
				echo '<td><span>1</span></td>';
				echo '<td><span>1</span></td>';
				}
			
			//*********************Recorrer los dias seleccionados para ver si hay visita ese dia ****////
			$total_visit=0;
			$desde2=$desde;
			while(strtotime($desde2)<=strtotime($hasta)) { 
			
			
				$fecha=explode('-',$desde2);
				$n_dia=$manager->diaSemana($fecha[0],$fecha[1],$fecha[2]);
				
				if($n_dia<=6 || $n_dia>=1)
				{
					
					$q_visit="select * from tiendasVisitadas where idTienda='".$dato_tienda['idTienda']."' and fecha='".$desde2."'";
					
					$r_visit=$manager->ejecutarConsulta($q_visit);
					
					$d_visit=mysqli_fetch_array($r_visit);
					
					if($d_visit['idTiendasVisitadas']!=NULL && $d_visit['idTiendasVisitadas']!="")
					{
						echo '<td align="center" bgcolor="#58FA58" width="20"><span></span></td>';	
						
						$total_visit++;
					}
					else
					{
						echo '<td align="center" width="5"><span></span></td>';
						}
					
				}
				
				$desde2 = date("d-m-Y", strtotime( "$desde2 + 1 DAY")) ; 
			} ///***********************Termina ciclo de Dias
			
			//////////////////////////////****Agrega links de las fotos *****/////////////////
			$q_fotos="Select * from photoCatalogo 
			where (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
			and id_tienda='".$dato_tienda['idTienda']."'";
			
			$n_marc=count($id_marca);
			$k=0;
				if($n_marc>0)
				{
						foreach($id_marca as $marcas)
						{
						   if($k==0)
						  {
							$q_fotos.="and (id_marca='".$marcas."' ";
						  }
						else
						{
							$q_fotos.=" or id_marca='".$marcas."'";
							}
							$k++;
						}
						$q_fotos.=")";
				}
			
			$rs_fotos=$manager->ejecutarConsulta($q_fotos);
	
			echo '<td align="left">';
			while($d_fotos=mysqli_fetch_array($rs_fotos))
			{
				echo '<a target="_blank" href="http://plataformavanguardia.net/'.$d_fotos['imagen'].'">Foto_'.$d_fotos['idphotoCatalogo'].'</a> ';
				}
			echo '</td>';
			
			
			///************ Totales de Visitas dependiendo el tipo de tienda ****//
			
			if($dato_ruta['idTipoTienda']==2)
			{
				$visit_res=$total_visit-3;
				}
			else
			{
				$visit_res=$total_visit-1;
				}
				
			if($visit_res>=0)
			{
				echo '<td align="center" width="15px" bgcolor="#01DF01"><span></span></td>';
				}
			else
			{
				echo '<td align="center" width="15px" bgcolor="#FF0000"><span></span></td>';
				}
			
			echo '<td align="center"><span>'.$total_visit.'</span></td>';
		echo '</tr>';
	
		usleep(1000);
	}
}
/////****************Final While Tiendas
  
 echo '</table>';// Cierra Tabla Principal

}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



