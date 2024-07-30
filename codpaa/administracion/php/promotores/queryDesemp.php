<?
ob_start();
session_start();

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$idSem=$_REQUEST['idSem'];
if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	
	$manager = new bdManager();
	?>
	<input type="hidden" id="idSemana" name="idSemana" value="<? echo $idSem;?>">
    
	<div id="porcent_vist" style="width: 280px; height: 80px;"></div>
	  
    <div id="det_vist" style="margin-top:15%">
    	<span><strong>Agrupar por fecha</strong></span>
        <input type="checkbox" id="group" name="group" onClick="group_dat();"/>	
		<?
        $sql_sem="Select semana,anio,idPromotor from supervisionRutas where idSupervision='".$idSem."'";
        $res_sem=$manager->ejecutarConsulta($sql_sem);
        $dat_sem=mysqli_fetch_array($res_sem);
        
        //***** calculamos las fechas de la semana
        $dias_semana=$manager->diasSemana($dat_sem['anio'],$dat_sem['semana']);
        
        $sql_ruta="select mt.*,sp.idTienda,f.grupo
        from supervisionRutas sp 
        left join maestroTiendas mt on (mt.idTienda=sp.idTienda) 
        left join tiendas_formatos f on (f.idFormato=mt.idFormato)
        where semana='".$dat_sem['semana']."' and anio='".$dat_sem['anio']."' and idPromotor='".$dat_sem['idPromotor']."'
        group by sp.idTienda";
        
        $rs_ruta=$manager->ejecutarConsulta($sql_ruta);
        
        ?>
        <input type="hidden" id="idPromo" name="idPromo" value="<? echo $dat_sem['idPromotor'];?>">
        <table id="productividad_det" width="auto">
    
            <thead>
                <tr>
                    <th data-options="field:'idTienda'">ID Tienda</th>
                    <th data-options="field:'sucursal'">Sucursal</th>
                    <th data-options="field:'distancia',align:'center'">Distancia (km)</th>
                    <th data-options="field:'fecha'">Fecha</th>
                    <th data-options="field:'hora'">Hora</th>
                    <th data-options="field:'tipo'">Tipo Registro</th>
                    <th data-options="field:'grupo'">Grupo</th>
                </tr>
            </thead>
            <tbody>
        <?
        //************* Recorremos la ruta de la semana elegida
        while($dat_ruta=mysqli_fetch_array($rs_ruta))
        {
            
            for($i=0;$i<=6;$i++)
            {
                $sql_vis="Select *
                from tiendasVisitadas 
                where fecha='".$dias_semana[$i]."'  
                and idTienda='".$dat_ruta['idTienda']."' and idCelular='".$dat_sem['idPromotor']."'
                order by idTiendasVisitadas asc";
                
                $rs_vis=$manager->ejecutarConsulta($sql_vis);
                
                while($dat_vis=mysqli_fetch_array($rs_vis))
                {
                ?>
                    <tr>
                        <td><? echo $dat_ruta['idTienda'];?></td>
                        <td><? echo $dat_ruta['sucursal'];?></td>
                        <td><? 
                        
                        $distancia=$manager->Distancia($dat_ruta['x'],$dat_ruta['y'],$dat_vis['latitud'],$dat_vis['longitud'],'K');
                        echo round($distancia,4);?></td>
                        <td><? echo $dat_vis['fecha'];?></td>
                        <td><? echo $dat_vis['hora'];?></td>
                        <td><? echo $dat_vis['tipo'];?></td>
                        <td><? echo $dat_ruta['grupo'];?></td>
                     </tr>
                
                <?
                }
            }
        }
        ?>
        </tbody>
    
     </table>
 </div>
 
 <div id="dlg_rastDs" style="padding:10px 20px;display: none;" >
   </div>
  <script language="javascript" type="application/javascript">
$('#productividad_det').datagrid({
	view:groupview,
	groupField:'idTienda',
	groupFormatter:function(value,rows){
                    return value + ' idtienda - ' + rows.length + ' Item(s)';
                },
	 rowStyler:function(index,row){
        if (row.distancia>1){
            return 'background-color:#ffee00;color:red;font-weight:bold;';
        }
   		 },
	onDblClickRow: function(index,row)
	{
		muestra_rast(row.fecha);
		}
		
	});
	
	function group_dat()
	{
		var value_group='idTienda';
		if(document.getElementById('group').checked)
		{
			value_group='fecha';	
			}
		$('#productividad_det').datagrid({
			view:groupview,
			groupField:value_group,
			groupFormatter:function(value,rows){
			return value +' '+value_group +' - ' + rows.length + ' Item(s)';
                }
		});
	}
	
	function muestra_rast(fecha)
	{
		var idPromotor=$('#idPromo').val();
		document.getElementById('dlg_rastDs').style.display="";
		
		$('#dlg_rastDs').dialog({
		 title: 'Rastreo Entradas',
		 href: '../php/promotores/rastreo_prod.php?idPromotor='+idPromotor+'&fecha='+fecha,
		 width: 650,
		 height: 600,
		buttons: [{
					text:'Ok',
					iconCls:'icon-ok',
					handler:function(){
						$('#dlg_rastDs').dialog('close');	
					}
				}]
			});
			
		}
		
		function carga_mapa()
		{
			var mapa;

    		initializeP();

			var idCel=$('#idPromoP').val();
			var fecha=$('#fechaP').val();
			
			            
				$.ajax({

					
                    url: '../../php/rastreo/getVisitas.php',

                    data: {id: idCel,fecha: fecha},

                    type: 'GET',

                    dataType: 'json',
					
					beforeSend: function(){

                        $("<img src='../imagenes/loading.gif' style='position:relative;" +

                        "top:0px;left:50px;z-index:2000' id='loading-imagen' />").appendTo("#Mapa-rastreoP")

                    },

                    success: function(json){

                        if(json.length > 0){

                            iniciarMapaP(json);
							

                        }else{



                            if(markerGeneral != null){

                                markerGeneral.setMap(null);



                                mapa.setZoom(5);

                                mapa.setCenter(new google.maps.LatLng(22.35006135229113,-101.55759218749995));

                            }

							$.messager.alert('Rastreo',"No existen registros de este Promotor en la fecha: "+setFecha,'info');

                            console.log('no hay registros');
							
                        }

							OnRastreo=false;
							RastreoName="";	

                    },

                    error: function(jqXHR,status,error){

                        console.log("error, getVisitas");
						OnRastreo=false;
						RastreoName="";



                    },

                   complete: function(jqXHR, status){

                        $("#loading-imagen").remove();

                        console.log("peticion getVisitas terminada");
						OnRastreo=false;
						RastreoName="";

                    }

                });
	
			
	}
</script> 
    <?
	
	
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>