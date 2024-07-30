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
				$filtro.=" and (pc.id_marca='".$idmarcas."' ";
				
			  }
			else
			{
				$filtro.=" or pc.id_marca='".$idmarcas."'";
				
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
//*******************Query de busqueda de Fotos
	
	$sql_fot="SELECT pc.idphotoCatalogo,concat(m.grupo,' ',m.sucursal,' (',m.idTienda,')') as nombreTienda,e.nombre, 
	pro.nombre as nombrePromo,pc.fecha, pc.imagen, tp.descripcion as nombreTipo,concat(sup.nombreSupervisor,' ',apellidoSupervisor) as nombreSuper

                From photoCatalogo as pc

                inner join maestroTiendas as m on pc.id_tienda=m.idTienda

                inner join tipoExhibicion as e on pc.id_exhibicion=e.idExhibicion

                inner join Promotores as pro on pc.id_promotor=pro.idCelular
				
				left join Supervisores as sup on sup.idSupervisores=pro.Supervisor

                left join tipoPromotor as tp on pro.idtipoPromotor=tp.idtipoPromotor

                where m.idTienda='".$idTienda."' and 
				(STR_TO_DATE(pc.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
				and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
				".$filtro."";
				
	
	$result_fot=$manager->ejecutarConsulta($sql_fot);
	
	
 $sql_tienda="select t.*,e.nombre from maestroTiendas t left join estados e on (t.idEstado=e.id) 
 where idTienda='".$idTienda."'";
 $resul_tienda=$manager->ejecutarConsulta($sql_tienda);
 $datos_tienda=mysqli_fetch_array($resul_tienda);
 
?>
<h3><? echo "Tienda <br/>".$datos_tienda['sucursal']." ".$datos_tienda['direccion']." ".utf8_encode($datos_tienda['estado']);?></h3>
<table id="fot_det_may" width="100%" >

		<thead>
            <tr>
                <th data-options="field:'idphotoCatalogo'">ID PHOTO</th>
                <th data-options="field:'imagen',hidden:true">imagen</th>
                <th data-options="field:'nombreTienda'">Nombre Tienda</th>
                <th data-options="field:'nombre',align:'center'">Exhibicion</th>
                <th data-options="field:'nombrePromo'">Promotor</th>
                <th data-options="field:'nombreSuper'">Supervisor</th>
                <th data-options="field:'fecha'">Fecha Captura</th>
                <th data-options="field:'nombreTipo'">Tipo Promotor</th>
                
            </tr>
        </thead>
		<tbody>
        
        <? while($datos_fot=mysqli_fetch_array($result_fot)){?>
             <tr>
                <td><? echo $datos_fot['idphotoCatalogo'];?></td>
                <td><? echo $datos_fot['imagen'];?></td>
                <td><? echo $datos_fot['nombreTienda'];?></td>
                <td><? echo $datos_fot['nombre'];?></td>
                <td><? echo $datos_fot['nombrePromo'];?></td>
                <td><? echo $datos_fot['nombreSuper'];?></td>
                <td><? echo $datos_fot['fecha'];?></td>
                <td><? echo $datos_fot['nombreTipo'];?></td>
                
              </tr>
            
        <? }?>    
  </tbody>

</table>
 
 <script >
 $('#fot_det_may').datagrid({
	view:cardview,

    singleSelect:true,
	
	onDblClickRow:function(rowIndex,rowData){
			crearDialogoFoto2(rowData.idphotoCatalogo);

    }

	});
</script>  
</body>

</html>

