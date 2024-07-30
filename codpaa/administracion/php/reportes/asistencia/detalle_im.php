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
$Desde= date ('d-m-Y',$_GET['Desde']);
$Hasta= date('d-m-Y',$_GET['Hasta']);
$idProm=$_GET['idProm'];
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
			and (STR_TO_DATE(im.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$Desde."','%d-%m-%Y') and STR_TO_DATE('".$Hasta."','%d-%m-%Y') )
			".$filtro." and im.idPromotor='".$idProm."'";
	
	$result_im=$manager->ejecutarConsulta($sql_im);
	
	
 $sql_tienda="select t.*,e.nombre from maestroTiendas t left join estados e on (t.idEstado=e.id) 
 where idTienda='".$idTienda."'";
 $resul_tienda=$manager->ejecutarConsulta($sql_tienda);
 $datos_tienda=mysqli_fetch_array($resul_tienda);
 
 $sql_prom="select * from Promotores where idCelular='".$idProm."'";
 $resul_prom=$manager->ejecutarConsulta($sql_prom);
 $datos_prom=mysqli_fetch_array($resul_prom);
 
?>
<h3><? echo "Tienda:  ".$datos_tienda['sucursal']." ".$datos_tienda['direccion']." ".$datos_tienda['estado'];?></h3>
<h3><? echo "Promotor: ".$datos_prom['nombre'];?></h3>

<table id="im_det" width="100%" >

		<thead>
            <tr>
                <th data-options="field:'idInteligencia'">ID</th>
                <th data-options="field:'Fecha'">Fecha</th>
                <th data-options="field:'Marca',align:'center'">Marca</th>
                <th data-options="field:'Producto'">Producto</th>
                <th data-options="field:'precioNormal'">Precio Normal</th>
                <th data-options="field:'precioOferta'">Precio Oferta</th>
                <th data-options="field:'ofertaCruzada'">Oferta Cruzada</th>
                <th data-options="field:'ProductoExtra'">Prod. Extra</th>
                <th data-options="field:'ProductoEmplayado'">Prod. Emplayado</th>
                <th data-options="field:'cambioImagen'">Cambio Imagen</th>
                <th data-options="field:'inicio_oferta'">Inicio Oferta</th>
                <th data-options="field:'fin_oferta'">Fin Oferta</th>
                <th data-options="field:'precio_caja'">Precio Caja</th>
                <th data-options="field:'cambio_precio'">Cambio Precio</th>
            </tr>
        </thead>
		<tbody>
        
        <? while($datos_im=mysqli_fetch_array($result_im)){?>
             <tr>
                <td><? echo $datos_im['idInteligencia'];?></td>
                <td><? echo $datos_im['fecha'];?></td>
                <td><? echo $datos_im['Marca'];?></td>
                <td><? echo $datos_im['nombreProd'];?></td>
                <td><? echo $datos_im['precioNormal'];?></td>
                <td><? echo $datos_im['PrecioOferta'];?></td>
                <td><? echo $datos_im['ofertaCruzada'];?></td>
                <td><? echo $datos_im['ProductoExtra'];?></td>
                <td><? echo $datos_im['ProductoEmplayado'];?></td>
                <td><? echo $datos_im['cambioImagen'];?></td>
                <td><? echo $datos_im['inicio_oferta'];?></td>
                <td><? echo $datos_im['fin_oferta'];?></td>
                <td><? echo $datos_im['precio_caja'];?></td>
                <td><? echo $datos_im['cambio_precio'];?></td>
            </tr>
            
        <? }?>    
  </tbody>

</table>
 
 <script language="javascript" type="application/javascript">
$('#im_det').datagrid();
</script>  
</body>

</html>

