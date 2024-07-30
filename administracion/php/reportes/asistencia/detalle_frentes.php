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
				$filtro.=" and (f.idMarca='".$idmarcas."' ";
				
			  }
			else
			{
				$filtro.=" or f.idMarca='".$idmarcas."'";
				
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
//*******************Query de busqueda de Frentes

 $sql_fren= "Select f.*,concat(p.nombre,'',p.presentacion) as nombreProd,m.nombre as Marca from frentesCharola f
 	inner join Producto p on (p.idProducto=f.idProducto)
	inner join Marca m on (m.idMarca=f.idMarca)
	where f.idTienda='".$idTienda."' 
	and (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$Desde."','%d-%m-%Y') and STR_TO_DATE('".$Hasta."','%d-%m-%Y') ) 
	".$filtro." and f.idCelular='".$idProm."'";
				
	$result_fren=$manager->ejecutarConsulta($sql_fren);
	
	
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

<table id="frentes_det" width="100%">

		<thead>
            <tr>
                <th data-options="field:'idfrente'">ID</th>
                <th data-options="field:'Fecha'">Fecha</th>
                <th data-options="field:'Marca',align:'center'">Marca</th>
                <th data-options="field:'Producto'">Producto</th>
                <th data-options="field:'cha1'">Charola 1</th>
                <th data-options="field:'cha2'">Charola 2</th>
                <th data-options="field:'cha3'">Charola 3</th>
                <th data-options="field:'cha4'">Charola 4</th>
                <th data-options="field:'cha5'">Charola 5</th>
                <th data-options="field:'cha6'">Charola 6</th>
            </tr>
        </thead>
		<tbody>
        
        <? while($datos_fren=mysqli_fetch_array($result_fren)){?>
             <tr>
                <td><? echo $datos_fren['idFrentesCharola'];?></td>
                <td><? echo $datos_fren['fecha'];?></td>
                <td><? echo $datos_fren['Marca'];?></td>
                <td><? echo $datos_fren['nombreProd'];?></td>
                <td><? echo $datos_fren['cha1'];?></td>
                <td><? echo $datos_fren['cha2'];?></td>
                <td><? echo $datos_fren['cha3'];?></td>
                <td><? echo $datos_fren['cha4'];?></td>
                <td><? echo $datos_fren['cha5'];?></td>
                <td><? echo $datos_fren['cha6'];?></td>
            </tr>
            
        <? }?>    
         </tbody>

 </table>
 
 <script language="javascript" type="application/javascript">
$('#frentes_det').datagrid();
</script>  
</body>

</html>

