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
	 
	$desde = $_REQUEST['Desde'];
	  
	$hasta = $_REQUEST['Hasta'];
	
	$id_marca=$_REQUEST['idMarca'];
	
	$tiposProd=$_REQUEST['tiposProd'];
	
	$idUsuario=$_SESSION['idUser'];

	$manager = new bdManager();
	
	$filtro="";
	$n_for=count($id_formato);
	$k=0;
	if($n_for>0)
	{
		foreach($id_formato as $formatos)
		{
		   if($k==0)
		  {
			$filtro.=" where (idFormato='".$formatos."'";
		  }
		else
		{
			$filtro.=" or idFormato='".$formatos."'";
			}
			$k++;
	    }
		$filtro.=")";
	}
	
	
	$filtro2="";
	
	$n_tip=count($tiposProd);
	$j=0;
	if($n_tip>0)
	{
		foreach($tiposProd as $tipos_prod)
		{
			
			if($i==0)
			{
				$filtro2.=" and (p.tipo='".$tipos_prod."'";	
			}
			else
			{
				$filtro2.=" or p.tipo='".$tipos_prod."'";	
			}
			$i++;
		}	
		$filtro2.=")";
		
		}

    
	
	//******************************************************************************************//
	$query_marca="select nombre from Marca where idMarca='".$id_marca."'";
	$resul_marca=$manager->ejecutarConsulta($query_marca);
	$datos_marca=mysqli_fetch_array($resul_marca);
	//***********Si se tiene el logo de la marca ******//
	if(file_exists('../../imagenes/logos/'.$datos_marca['nombre'].'.png'))
	{
		echo '<img src="../../imagenes/logos/'.$datos_marca['nombre'].'.png" height="200px" />';
	}
	
			echo '<table width="100%" border="1" bordercolor="#D8D8D8">
			<tr bgcolor="#A4A4A4">
			<td><span>ID PROD</span></td>
			<td><span>NOMBRE</span></td>
			<td><span>CODIGO</span></td>
			<td><span>TIPO</span></td>
			<td><span>MODELO</span></td>
			';
			$sql_formatos="select idFormato,concat(cadena,' ',grupo) as nom_formato from tiendas_formatos ".$filtro."";
		
			$rs_formatos=$manager->ejecutarConsulta($sql_formatos);
		
			while($dat_formatos=mysqli_fetch_array($rs_formatos))
			{
				echo '<td>'.$dat_formatos['nom_formato'].'</td>';
				}
			
			echo '</tr>';										
	
	$sql_prods="select p.idProducto,p.idMarca,concat(p.nombre, ' ',p.presentacion) as prod_nom,p.codigoBarras,p.tipo,p.modelo from Producto p 
	where p.idMarca='".$id_marca."' ".$filtro2."";
	
	$rs_prods=$manager->ejecutarConsulta($sql_prods);
	
	while($dato_prods=mysqli_fetch_array($rs_prods))
	{
		
			echo '
			<tr bgcolor="#D8D8D8">
			<td><span>'.$dato_prods['idProducto'].'</span></td>
			<td><span>'.$dato_prods['prod_nom'].'</span></td>
			<td><span>'.$dato_prods['codigoBarras'].'</span></td>
			<td><span>'.$dato_prods['tipo'].'</span></td>
			<td><span>'.$dato_prods['modelo'].'</span></td>
			';
			
			
			$sql_formatos="select idFormato,concat(cadena,' ',grupo) as nom_formato from tiendas_formatos ".$filtro."";
		
			$rs_formatos=$manager->ejecutarConsulta($sql_formatos);
			
		
		///// **********************************Ciclo Formatos
			while($dato_formatos=mysqli_fetch_array($rs_formatos))
			{
				$sql_normal = "select SUM(im.precioNormal) as tot_norm
				,count(idInteligencia) as tot_num
				from inteligenciaMercado im
				inner join maestroTiendas t on (im.idTienda=t.idTienda)
				where im.precioOferta='---' and im.idProducto='".$dato_prods['idProducto']."' and 
				(STR_TO_DATE(im.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
				and t.idFormato='".$dato_formatos['idFormato']."' 
				and (((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))<=140
				or ((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))>=80)
				GROUP BY im.idProducto";
				
				$rs_normal=$manager->ejecutarConsulta($sql_normal);
				
				$dato_normal=mysqli_fetch_array($rs_normal);
				
				$sql_oferta = "select SUM(im.precioOferta) as tot_ofer
				,count(idInteligencia) as tot_num
				from inteligenciaMercado im
				inner join maestroTiendas t on (im.idTienda=t.idTienda)
				where im.precioOferta!='---' and im.idProducto='".$dato_prods['idProducto']."' and 
				(STR_TO_DATE(im.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
				and t.idFormato='".$dato_formatos['idFormato']."' 
				and (((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))<=140
				or ((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))>=80)
				GROUP BY im.idProducto
				";
				
				$rs_oferta=$manager->ejecutarConsulta($sql_oferta);
				
				$dato_oferta=mysqli_fetch_array($rs_oferta);
				
				
				$tot_precio=$dato_normal['tot_norm']+$dato_oferta['tot_ofer'];
				
				$tot_im=$dato_normal['tot_num']+$dato_oferta['tot_num'];
				
				if($tot_im>0)
				{
					$prom_im=$tot_precio/$tot_im;
				}
				else
				{
					$prom_im=0;
					}
				
				if($prom_im>0)
				{
				echo '
				<td bgcolor="#58D3F7"><span>'.round($prom_im,2).'</span></td>
				';
				}
				else
				{
				echo '
				<td bgcolor="#2E2EFE"><span>---</span></td>
				';
					}
					
		
			
		}/////******Final While Formatos
		echo '</tr>';
		
		
	}////********Final While Productos
		   
 	echo '</table>';// Cierra Tabla Principal

}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}
