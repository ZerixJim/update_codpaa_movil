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
$Mes= $_GET['Mes'];
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
//*******************Query de busqueda de Ventas Promedio (surtido)

	 $sql_vtas="Select sm.*,concat(p.nombre,'',p.presentacion) as nombreProd,m.nombre as Marca
		from surtidoMueble sm 
		inner join Producto p on (sm.idProducto=p.idProducto)
		inner join Marca m on (m.idMarca=p.idMarca) 
		where sm.idTienda='".$idTienda."' 
		and (STR_TO_DATE(sm.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
		and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
		".$filtro." ";		
	
	$result_vtas=$manager->ejecutarConsulta($sql_vtas);
	
	
 $sql_tienda="select t.*,e.nombre from maestroTiendas t left join estados e on (t.idEstado=e.id) 
 where idTienda='".$idTienda."'";
 $resul_tienda=$manager->ejecutarConsulta($sql_tienda);
 $datos_tienda=mysqli_fetch_array($resul_tienda);
 
?>
<h3><? echo "Tienda:  ".$datos_tienda['sucursal']." ".$datos_tienda['direccion']." ".$datos_tienda['estado'];?></h3>
<div class="table-responsive-vertical shadow-z-1">
<table id="vtaP_det" class="table table-hover table-mc-light-green">

		<thead>
            <tr bgcolor="#64B5F6">
                <th >ID</th>
                <th >Fecha</th>
                <th >Marca</th>
                <th >Producto</th>
                <th >Cajas</th>
                <th >Promotor</th>
            </tr>
        </thead>
		<tbody>
        
        <? $cjas_tot=0;
		while($datos_vtas=mysqli_fetch_array($result_vtas)){?>
             <tr>
                <td data-title="ID"><? echo $datos_vtas['idsurtidoMueble'];?></td>
                <td data-title="Fecha"><? echo $datos_vtas['fecha'];?></td>
                <td data-title="Marca"><? echo $datos_vtas['Marca'];?></td>
                <td data-title="Producto"><? echo $datos_vtas['nombreProd'];?></td>
                <td data-title="Cajas"><? 
				$cjas_tot+=$datos_vtas['cajas'];
				echo $datos_vtas['cajas'];?></td>
                <td data-title="Promotor"><? echo $datos_vtas['idCelular'];?></td>
            </tr>
            
        <? }?> 
        <tr bgcolor="#FFA726">
        	<td data-title="ID"></td>
            <td data-title="Fecha"></td>
            <td data-title="Marca"></td>
            <td data-title="Producto"><? echo "Total";?></td>
            <td data-title="Cajas"><? echo $cjas_tot;?></td>
            <td data-title="Promotor"></td>
        </tr>
           
  </tbody>

</table>
 
 <script language="javascript" type="application/javascript">
/*$('#vtaP_det').datagrid({showFooter: true});

$('#vtaP_det').datagrid('reloadFooter',[
	{Producto:'Total',cajas: <? //echo $cjas_tot;?>}
]);*/
</script>  
</body>

</html>

