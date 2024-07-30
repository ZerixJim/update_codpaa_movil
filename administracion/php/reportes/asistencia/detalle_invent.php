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
//*******************Query de busqueda de Inventarios

	 $sql_invent="Select i.*,concat(p.nombre,'',p.presentacion) as nombreProd,m.nombre as Marca
		from inventarioBodega i 
		inner join Producto p on (i.idProducto=p.idProducto)
		inner join Marca m on (m.idMarca=p.idMarca) 
		where i.idTienda='".$idTienda."' 
		and (STR_TO_DATE(i.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$Desde."','%d-%m-%Y') and STR_TO_DATE('".$Hasta."','%d-%m-%Y') )
		".$filtro." and i.idPromotor='".$idProm."'";		
	
	$result_invent=$manager->ejecutarConsulta($sql_invent);
	
	
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

<table id="invent_det" width="auto" >

		<thead>
            <tr>
                <th data-options="field:'idInventario'">ID</th>
                <th data-options="field:'Fecha'">Fecha</th>
                <th data-options="field:'Marca',align:'center'">Marca</th>
                <th data-options="field:'Producto'">Producto</th>
                <th data-options="field:'cantFis'">Fisico</th>
                <th data-options="field:'cantSis'">Sistema</th>
                <th data-options="field:'tipo'">Tipo</th>
                <th data-options="field:'lote'">Lote</th>
                <th data-options="field:'caducidad'">Caducidad</th>
            </tr>
        </thead>
		<tbody>
        
        <? while($datos_invent=mysqli_fetch_array($result_invent)){?>
             <tr>
                <td><? echo $datos_invent['idInventario'];?></td>
                <td><? echo $datos_invent['fecha'];?></td>
                <td><? echo $datos_invent['Marca'];?></td>
                <td><? echo $datos_invent['nombreProd'];?></td>
                <td><? echo $datos_invent['cantidadFisico'];?></td>
                <td><? echo $datos_invent['cantidadSistema'];?></td>
                <td><? echo $datos_invent['tipo'];?></td>
                <td><? echo $datos_invent['lote'];?></td>
                <td><? echo $datos_invent['fecha_caducidad'];?></td>
            </tr>
            
        <? }?>    
  </tbody>

</table>
 
 <script language="javascript" type="application/javascript">
$('#invent_det').datagrid();
</script>  
</body>

</html>

