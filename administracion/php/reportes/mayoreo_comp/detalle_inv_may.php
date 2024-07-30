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
//*******************Query de busqueda de Inventarios

	 $sql_invent="Select i.*,concat(p.nombre,'',p.presentacion) as nombreProd,m.nombre as Marca
		from inventarioBodega i 
		inner join Producto p on (i.idProducto=p.idProducto)
		inner join Marca m on (m.idMarca=p.idMarca) 
		where i.idTienda='".$idTienda."' and i.tipo='Cajas'
		and (STR_TO_DATE(i.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
		and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
		".$filtro." ";		
	
	$result_invent=$manager->ejecutarConsulta($sql_invent);
	
	
 $sql_tienda="select t.*,e.nombre from maestroTiendas t left join estados e on (t.idEstado=e.id) 
 where idTienda='".$idTienda."'";
 $resul_tienda=$manager->ejecutarConsulta($sql_tienda);
 $datos_tienda=mysqli_fetch_array($resul_tienda);
 
?>
<h3><? echo "Tienda </br> ".$datos_tienda['sucursal']." ".$datos_tienda['direccion']." ".utf8_encode($datos_tienda['estado']);?></h3>
<div class="table-responsive-vertical shadow-z-1">

 <table id="invent_det" class="table table-hover table-mc-light-green">

		<thead>
            <tr bgcolor="#64B5F6">
                <th >ID</th>
                <th >Fecha</th>
                <th >Marca</th>
                <th >Producto</th>
                <th >Fisico</th>
                <th >Sistema</th>
                <th >Tipo</th>
                <th >Lote</th>
                <th >Caducidad</th>
            </tr>
        </thead>
		<tbody>
        
        <? $fis_tot=0;
		   $sis_tot=0;
			while($datos_invent=mysqli_fetch_array($result_invent)){?>
             <tr>
                <td data-title="ID"><? echo $datos_invent['idInventario'];?></td>
                <td data-title="Fecha"><? echo $datos_invent['fecha'];?></td>
                <td data-title="Marca"><? echo $datos_invent['Marca'];?></td>
                <td data-title="Producto"><? echo $datos_invent['nombreProd'];?></td>
                <td data-title="Fisico"><? 
				$fis_tot+=$datos_invent['cantidadFisico'];
				 echo $datos_invent['cantidadFisico'];?></td>
                <td data-title="Sistema"><? 
				$sis_tot+=$datos_invent['cantidadSistema'];
				echo $datos_invent['cantidadSistema'];?></td>
                <td data-title="Tipo"><? echo $datos_invent['tipo'];?></td>
                <td data-title="Lote"><? echo $datos_invent['lote'];?></td>
                <td data-title="Caducidad"><? echo $datos_invent['fecha_caducidad'];?></td>
            </tr>
            
        <? } ?>
        <tr bgcolor="#FFA726">
        	<td data-title="ID"></td>
            <td data-title="Fecha"></td>
            <td data-title="Marca"></td>
            <td data-title="Producto"><? echo "Totales";?></td>
            <td data-title="Fisico"><? echo $fis_tot;?></td>
            <td data-title="Sistema"><? echo $sis_tot;?></td>
            <td data-title="Tipo"><? echo "Cajas";?></td>
            <td data-title="Lote"></td>
            <td data-title="Caducidad"></td>
        </tr>
         </tbody>

</table>
</div>
 
 <script language="javascript" type="application/javascript">
//$('#invent_det').datagrid({showFooter: true});

//$('#invent_det').datagrid('reloadFooter',[
	//{Producto:'Totales',cantFis: <? //echo $fis_tot;?>, cantSis: <? //echo $sis_tot;?>}
//]);
</script>  
</body>

</html>

