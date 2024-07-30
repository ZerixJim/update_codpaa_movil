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
$Marcas=explode(',',$_GET['Marcas']);

$n_marc=count($Marcas);
$k=0;
	if($n_marc>0)
	{
			foreach($Marcas as $idmarcas)
			{
			   if($k==0)
			  {
				$filtro.=" and (vp.idMarca='".$idmarcas."' ";
				
			  }
			else
			{
				$filtro.=" or vp.idMarca='".$idmarcas."'";
				
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
//*******************Query de busqueda de Ventas Oficiales (ventas_prom)

	 $sql_vtas="Select vp.*,m.nombre as Marca
		from venta_promedio vp 
		inner join Marca m on (m.idMarca=vp.idMarca) 
		where vp.idTienda='".$idTienda."' 
		and MONTH(fecha_inicio)='".$Mes."' and YEAR(fecha_inicio)='".$Anio."'
		".$filtro." ";		
	
	$result_vtas=$manager->ejecutarConsulta($sql_vtas);
	
	
 $sql_tienda="select t.*,e.nombre from maestroTiendas t left join estados e on (t.idEstado=e.id) 
 where idTienda='".$idTienda."'";
 $resul_tienda=$manager->ejecutarConsulta($sql_tienda);
 $datos_tienda=mysqli_fetch_array($resul_tienda);
?>
<h3><? echo "Tienda:  ".$datos_tienda['sucursal']." ".$datos_tienda['direccion']." ".$datos_tienda['estado'];?></h3>
<div class="table-responsive-vertical shadow-z-1">
<table id="vtaOf_det" class="table table-hover table-mc-light-green">

		<thead>
            <tr bgcolor="#64B5F6">
                <th >Fecha Inicio</th>
                <th >Fecha Fin</th>
                <th >Marca</th>
                <th >Cantidad</th>
                <th >Tipo</th>
                <th >Promotor</th>
            </tr>
        </thead>
		<tbody>
        
        <? 
		$tot_pzas=0;
		$tot_cjas=0;
		$tot_monto=0;
		while($datos_vtas=mysqli_fetch_array($result_vtas)){?>
             <tr>
                <td data-title="Fecha Inicio"><? echo $datos_vtas['fecha_inicio'];?></td>
                <td data-title="Fecha Fin"><? echo $datos_vtas['fecha_fin'];?></td>
                <td data-title="Marca"><? echo $datos_vtas['Marca'];?></td>
                <td data-title="Cantidad"><? 
				if($datos_vtas['tipo']=='CAJAS')
				{
					$tot_cjas+=$datos_vtas['cantidad'];
					}
				else if($datos_vtas['tipo']=='PIEZAS')
				{
					$tot_pzas+=$datos_vtas['cantidad'];
					}
				else if($datos_vtas['tipo']=='MONTO')
				{
					$tot_monto+=$datos_vtas['cantidad'];
					}
				echo $datos_vtas['cantidad'];?></td>
                <td data-title="Tipo"><? echo $datos_vtas['tipo'];?></td>
                <td data-title="Promotor"><? echo $datos_vtas['idPromotor'];?></td>
            </tr>
            
        <? }?>  
        <tr bgcolor="#FFA726">
        	<td data-title="Fecha Inicio"></td>
            <td data-title="Fecha Fin"></td>
            <td data-title="Marca"><? echo "Totales";?></td>
            <td data-title="Cantidad"><? echo $tot_cjas;?></td>
            <td data-title="Tipo"><? echo "Cajas";?></td>
            <td data-title="Promotor"></td>
        </tr>
        <tr bgcolor="#FFA726">
        	<td data-title="Fecha Inicio"></td>
            <td data-title="Fecha Fin"></td>
            <td data-title="Marca"></td>
            <td data-title="Cantidad"><? echo $tot_pzas;?></td>
            <td data-title="Tipo"><? echo "Pzas";?></td>
            <td data-title="Promotor"></td>        
        </tr>
        <tr bgcolor="#FFA726">
        	<td data-title="Fecha Inicio"></td>
            <td data-title="Fecha Fin"></td>
            <td data-title="Marca"></td>
            <td data-title="Cantidad"><? echo $tot_monto;?></td>
            <td data-title="Tipo"><? echo "Monto";?></td>
            <td data-title="Promotor"></td>          
        </tr>
          
  </tbody>

</table>
</div> 
 <script language="javascript" type="application/javascript">
/*$('#vtaOf_det').datagrid({showFooter: true});

$('#vtaOf_det').datagrid('reloadFooter',[
	{Marca:'Totales',cantidad: <? //echo $tot_cjas;?>,tipo:'Cajas'},
	{cantidad:<? //echo $tot_pzas;?>,tipo:'Piezas'},
	{cantidad:<? //echo $tot_monto;?>,tipo:'Monto'}
]);*/
</script>  
</body>

</html>

