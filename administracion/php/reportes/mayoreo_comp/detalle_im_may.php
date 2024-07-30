<?php

/**

 * Created by Dreamweaver.

 * User: Christian

 * Date: 14/01/16

 * Time: 11:20

 */

ob_start();
session_start();


include_once('../../../connexion/bdManager.php');
$manager = new bdManager();

$idTienda=$_GET['idTienda'];
$Mes=$_GET['Mes'];
$Anio= $_GET['Anio'];

$dias_mes=date('t',$Mes);

$mes=intval($Mes);

	 if($mes<10)
	 {
		 $mes="0".$mes;
		 }
	//Fechas del mes para consultas....
	$fecha_ini="01-".$mes."-".$Anio;
	
	$fecha_fin=$dias_mes."-".$mes."-".$Anio;


$Marcas=explode(',',$_GET['Marcas']);

$n_marc=count($Marcas);
$k=0;
	if($n_marc>0)
	{
			foreach($Marcas as $idmarcas)
			{
			   if($k==0)
			  {
				$filtro.=" and (p.idMarca='".$idmarcas."' ";
				
			  }
			else
			{
				$filtro.=" or p.idMarca='".$idmarcas."'";
				
				}
				$k++;
			}
			$filtro.=")";
	}

	
	
?>



<!Doctype html>

<html>

<head>

</head>

<body>

<? 
//*******************Query de busqueda de Inteligencia M
	
	$sql_im="Select im.*,concat(p.nombre,'',p.presentacion) as nombreProd,m.nombre as Marca
			from inteligenciaMercado im 
			inner join Producto p on (p.idProducto=im.idProducto)
			inner join Marca m on (m.idMarca=p.idMarca) 
			inner join maestroTiendas t on (t.idTienda=im.idTienda)
			where im.idTienda='".$idTienda."' 
			and (STR_TO_DATE(im.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
			and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
			".$filtro." ";
	
	$result_im=$manager->ejecutarConsulta($sql_im);
	
	
 $sql_tienda="select t.*,e.nombre from maestroTiendas t left join estados e on (t.idEstado=e.id) 
 where idTienda='".$idTienda."'";
 $resul_tienda=$manager->ejecutarConsulta($sql_tienda);
 $datos_tienda=mysqli_fetch_array($resul_tienda);
 
?>
<h3><? echo "Tienda </br> ".$datos_tienda['sucursal']." ".$datos_tienda['direccion']." ".utf8_encode($datos_tienda['estado']);?></h3>
<div class="table-responsive-vertical shadow-z-1">
<table id="im_det_may" class="table table-hover table-mc-light-green">

		<thead>
            <tr bgcolor="#64B5F6">
                <th>ID</th>
                <th>Fecha</th>
                <th >Marca</th>
                <th >Producto</th>
                <th >Precio Normal</th>
                <th >Precio Oferta</th>
                <th >Oferta Cruzada</th>
                <th >Prod Extra</th>
                <th >Prod Emplayado</th>
                <th >Cambio Imagen</th>
                <th >Inicio Oferta</th>
                <th >Fin Oferta</th>
                <th >Precio Caja</th>
                <th >Cambio Precio</th>
            </tr>
        </thead>
		<tbody>
        
        <? while($datos_im=mysqli_fetch_array($result_im)){?>
             <tr>
                <td data-title="ID"><? echo $datos_im['idInteligencia'];?></td>
                <td data-title="Fecha"><? echo $datos_im['fecha'];?></td>
                <td data-title="Marca"><? echo $datos_im['Marca'];?></td>
                <td data-title="Producto"><? echo $datos_im['nombreProd'];?></td>
                <td data-title="Precio Normal"><? echo $datos_im['precioNormal'];?></td>
                <td data-title="Precio Oferta"><? echo $datos_im['PrecioOferta'];?></td>
                <td data-title="Oferta Cruzada"><? echo $datos_im['ofertaCruzada'];?></td>
                <td data-title="Prod Extra"><? echo $datos_im['ProductoExtra'];?></td>
                <td data-title="Prod Emplayado"><? echo $datos_im['ProductoEmplayado'];?></td>
                <td data-title="Cambio Imagen"><? echo $datos_im['cambioImagen'];?></td>
                <td data-title="Inicio Oferta"><? echo $datos_im['inicio_oferta'];?></td>
                <td data-title="Fin Oferta"><? echo $datos_im['fin_oferta'];?></td>
                <td data-title="Precio Caja"><? echo $datos_im['precio_caja'];?></td>
                <td data-title="Cambio Precio"><? echo $datos_im['cambio_precio'];?></td>
            </tr>
            
        <? }?>    
  </tbody>

</table>
</div>
 
 <script language="javascript" type="application/javascript">
//$('#im_det_may').datagrid();
</script>  
</body>

</html>

