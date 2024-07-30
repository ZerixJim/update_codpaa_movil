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

	$manager = new bdManager();

	$id_estado = $_REQUEST['idEstado'];
	
	$regionmx = $_REQUEST['RegionMx'];
	
	$MesLab = $_REQUEST['MesLab'];
	
	$GrupoM=$_REQUEST['GrupoM'];
	  
	$fecha_c=explode('-',$MesLab);
	
	$dias_mes=date('t',$fecha_c[0]);
	
	//********datos de fecha actual
	$mes_act=date('m');
	
	$mes_ant=intval($mes_act)-1;
	
	if($mes_ant<10)
	{
		$mes_ant="0".$mes_ant;
	}
	
	$anio_act=date('Y');
	
	$dia_act=date('j');
	//*****************************
	
	$mes=intval($fecha_c[0]);

	 if($mes<10)
	 {
		 $mes="0".$mes;
		 }
	//Fechas del mes para consultas....
	$fecha_ini="01-".$mes."-".$fecha_c[1];
	
	$fecha_fin=$dias_mes."-".$mes."-".$fecha_c[1];
	
	$mes_vent=intval($fecha_c[0])-1;
	
	$dias_mes_v=date('t',$mes_vent);
	
	if($mes_vent<10)
	{
		$mes_vent="0".$mes_vent;
	}
	if($fecha_c[0]==1)
	{
		$anio_vent=$fecha_c[1]-1;
	}
	else
	{
		$anio_vent=$fecha_c[1];	
		}
	//Fechas del mes para consulta de ventas...
	$fecha_ini_v="01-".$mes_vent."-".$anio_vent;
	$fecha_fin_v=$dias_mes_v."-".$mes_vent."-".$anio_vent;
	
	$id_marca=$_REQUEST['idMarca'];
	
	$filtro="";
		
	if($id_estado!="" && $id_estado!="all")
	{
		$filtro.=" and mT.idEstado='".$id_estado."'";
		}

		
	if($regionmx!="" && $regionmx!="all")
	{
		
		$query_est="select * from estados where id_region='".$regionmx."'";
		$rs_est=$manager->ejecutarConsulta($query_est);
		$n_est=0;
	    while($dat_est=mysqli_fetch_array($rs_est))
		{
		 	if($n_est==0)
			{
				$filtro.=" and (mT.idEstado='".$dat_est['id']."'";
				}  		
			else
			{
				$filtro.=" or mT.idEstado='".$dat_est['id']."'";
				}
			$n_est++;
		}
		
		$filtro.=")";
	}
	
	if($GrupoM!="" && $GrupoM!="TODOS")
	{
		$filtro.=" and mT.grupo='".$GrupoM."'";
		}
	

	$sql_tienda="Select DISTINCT mT.idTienda from cod_tienda_marca_promotor ctm  
	inner join maestroTiendas mT on (mT.idTienda=ctm.idTienda)
	where mT.idTipoTienda='2' and mes='".$fecha_c[0]."' and anio='".$fecha_c[1]."' and idMarca IN ( ";
	
	$n_marc=count($id_marca);
		$k=0;
			if($n_marc>0)
			{
					foreach($id_marca as $marcas)
					{
					    if($k==0)
					  {
						$sql_tienda.=" '".$marcas."' ";
					  }
					else
					{
						$sql_tienda.=" ,'".$marcas."'";
						}
						$k++;
					}
					$sql_tienda.=")";
			}
		$sql_tienda.=" ".$filtro."  
		GROUP BY ctm.idTienda
		order by mT.grupo ASC";
	
	   $rs_tienda=$manager->ejecutarConsulta($sql_tienda);
	   
	   $visitadas_tot=0;
	   $novis_tot=0;
	   $total_fot=0;
	   $total_inv=0;
	   $total_im=0;
	//Si es el mes actual.. CALCULA
   if(($mes==$mes_act && $anio_act==$fecha_c[1]) || ($mes==$mes_ant && intval($dia_act)<6))
   {
	   while($dat_tienda=mysqli_fetch_array($rs_tienda))
	   {
		   
						
		   // Revision de Visitas
		   $sql_vis="select COUNT(idTiendasVisitadas) as tot_vis from tiendasVisitadas 
			where (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
				  and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y')) and idTienda='".$dat_tienda['idTienda']."' and tipo='E'";
			
			$rs_vis=$manager->ejecutarConsulta($sql_vis);
			
			$dato_vis=mysqli_fetch_array($rs_vis);
			
			// Si se visito
			if($dato_vis['tot_vis']>0)
			{
				
				$visitadas_tot++;
				
			  // Si hay marcas en el filtro(obligatorio)
			  if($n_marc>0)
			  {
				  $fotos_tot=0;
				  $im_tot=0;
				  $inv_tot=0;
				  
				//*************Recorre las marcas seleccionadas
				foreach($id_marca as $marcas)
				{
					// Consultar las Fotos
					$query_fot="select count(idphotoCatalogo) as fot_tot from photoCatalogo ph 
									where (STR_TO_DATE(ph.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
									and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
									and ph.id_tienda='".$dat_tienda['idTienda']."' and ph.id_marca='".$marcas."'"
								;
					$rs_fot=$manager->ejecutarConsulta($query_fot);
					
					$dat_fot=mysqli_fetch_array($rs_fot);
					
					if($dat_fot['fot_tot']>0)
					{
						$fotos_tot+=$dat_fot['fot_tot'];
						}
					
					//Consultas IM
					$query_im="select count(idInteligencia) as im_tot from inteligenciaMercado im 
								left join Producto p on (p.idProducto=im.idProducto)
								where (STR_TO_DATE(im.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
								and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
								and p.idMarca='".$marcas."' and im.idTienda='".$dat_tienda['idTienda']."'"
								;
					$rs_im=$manager->ejecutarConsulta($query_im);
					
					$dat_im=mysqli_fetch_array($rs_im);
					
					if($dat_im['im_tot']>0)
					{
						$im_tot+=$dat_im['im_tot'];
						}
					
					//Consultar Inv	
					 $query_inv="select sum(cantidadFisico) as inv_tot from inventarioBodega inv 
									left join Producto p on (p.idProducto=inv.idProducto)
									where (STR_TO_DATE(inv.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') 
									and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y')) and inv.tipo='Cajas'
									and p.idMarca='".$marcas."' and inv.idTienda='".$dat_tienda['idTienda']."'"
								;
					$rs_inv=$manager->ejecutarConsulta($query_inv);
					
					$dat_inv=mysqli_fetch_array($rs_inv);
					
					if($dat_inv['inv_tot']>0)
					{
						$inv_tot+=$dat_inv['inv_tot'];
						}
					
				   } // Termina Foreach Marcas
				 }
				 $total_fot+=$fotos_tot;
				 $total_im+=$im_tot;
				 $total_inv+=$inv_tot;
				 
			}
			//Si no solo consulta la tabla mayoreo_comp
			else
			{
				$novis_tot++;
				}
	      
			
	} // Termina Ciclo de Tiendas
}
//Si el mes es anterior se consultan resultados
	 else
		  {
			  // Si hay marcas en el filtro(obligatorio)
			  if($n_marc>0)
			  {
				  $idMarca=0;
				//*************Recorre las marcas seleccionadas
				foreach($id_marca as $marcas)
				{
					$query_vis="select sum(inteligenciaM) as tot_im,sum(fotos) as tot_fot,sum(inventarios) as tot_inv,count(id_may_comp) as tot_vis 
					from mayoreo_comp mc 
					inner join maestroTiendas mT on (mT.idTienda=mc.idTienda)
					where mc.idMarca='".$marcas."' and visitada='1' and mc.mes='".$mes."' and mc.anio='".$fecha_c[1]."' ".$filtro."";
				
					$rs_vis=$manager->ejecutarConsulta($query_vis);
					
					$dat_vis=mysqli_fetch_array($rs_vis);
					
					$total_fot+=$dat_vis['tot_fot'];
					
					$total_im+=$dat_vis['tot_im'];
					
					$total_inv+=$dat_vis['tot_inv'];
					
					$idMarca=$marcas;
			
			
				}
					//Visitadas
					$query_vis="select count(DISTINCT mc.idTienda) as tot_vis 
					from mayoreo_comp mc 
					inner join maestroTiendas mT on (mc.idTienda=mT.idTienda)
					where idMarca='".$idMarca."' and visitada='1' and mes='".$mes."' and anio='".$fecha_c[1]."' ".$filtro."";
				
					$rs_vis=$manager->ejecutarConsulta($query_vis);
					
					$dat_vis=mysqli_fetch_array($rs_vis);
					
					//No Visitadas	
					$query_nvis="select count(DISTINCT mc.idTienda) as tot_nvis 
					from mayoreo_comp mc 
					inner join maestroTiendas mT on (mc.idTienda=mT.idTienda)
					where idMarca='".$idMarca."' and visitada='0' and mes='".$mes."' and anio='".$fecha_c[1]."' ".$filtro."";
				
					$rs_nvis=$manager->ejecutarConsulta($query_nvis);
					
					$dat_nvis=mysqli_fetch_array($rs_nvis);
				
					
					$visitadas_tot=$dat_vis['tot_vis'];
					$novis_tot=$dat_nvis['tot_nvis'];
			  }
			  
	     }
	   	
		$result_vist = array();  
		
		$result_vist[0]['Tipo']="Visitadas"; 
		$result_vist[0]['Total']=$visitadas_tot; 
		
		$result_vist[1]['Tipo']="NoVisitadas"; 
		$result_vist[1]['Total']=$novis_tot; 
		
		$vis_res=json_encode($result_vist,JSON_PARTIAL_OUTPUT_ON_ERROR); 
				
		echo ' <div width="100%" align="center">';
		?>
		<script type="application/javascript" language="javascript">
		$(document).ready(function(e) {
			console.log(<? echo $vis_res;?>);
          visitasGraph(<? echo $vis_res;?>); 
		  
		   // start all the timers
      $('.timer').countTo();
		});
		 </script> 
        <div id="asistenciaGra" style="width: 600px; height: 320px;"></div>
         <table width="100%">
         <tr>
         <td align="center">
             <div class="counter">
             <h5>Tiendas</h5>
               <p><b class="timer" id="tiend_tot" data-to="<? echo $visitadas_tot;?>" data-speed="1500"></b> Vis
               <b class="timer" id="tiend_tot" data-to="<? $total_tien=$visitadas_tot+$novis_tot;  
			   echo $novis_tot;?>" data-speed="1500"></b>NoVis</p>
               </div>
          </td>
         <td align="center">
             <div class="counter">
             <h5>Fotos</h5>
               <p><b class="timer" id="fotos_tot" data-to="<? echo $total_fot;?>" data-speed="1500"></b></p>
               </div>
          </td>
          <td align="center"> 
               <div class="counter">
             <h5>Inteligencia M </h5>
               <p><b class="timer" id="Im_tot" data-to="<? echo $total_im;?>" data-speed="1500"></b> Regs</p>
               </div>
           </td>
           <td align="center">
               <div class="counter">
             <h5>Inventarios </h5>
               <p><b class="timer" id="Inv_tot" data-to="<? echo $total_inv;?>" data-speed="1500"></b> Cajas</p>
               </div>
           </td>
           </table>
        <?
		
 echo '</div>';// Cierra Tabla Principal
?>

<?
}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



