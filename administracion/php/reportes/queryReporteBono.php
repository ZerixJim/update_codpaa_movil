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



    $idTienda = $_REQUEST['nombreTienda'];

    $Mes = $_REQUEST['Mes'];

    $Anio = $_REQUEST['Anio'];
	
	$Estado= $_REQUEST['Estado'];
	
	$num_det=$_REQUEST['n_fotos'];
	
	//Marcas
	$a_marcas=explode("&",$_REQUEST['Marca']);
	$n_marcas=count($a_marcas);
	for($mar=1;$mar<=$n_marcas;$mar++)
	{
		$idMarcas[]=$a_marcas[$mar];
		}
	
	//Detalles de fotos
	$a_fotosc=explode("&",$_REQUEST['fotos_c']);
	$n_fotosc=count($a_fotosc);
	for($fc=1;$fc<=$n_fotosc;$fc++){
	$fotos_c[]=$a_fotosc[$fc];	
	}
	
	$a_fotost=explode("&",$_REQUEST['fotos_t']);
	$n_fotost=count($a_fotost);
	for($ft=1;$ft<=$n_fotost;$ft++){
	$fotos_t[]=$a_fotost[$ft];	
	}
	
	$a_fotosl=explode("&",$_REQUEST['fotos_l']);
	$n_fotosl=count($a_fotosl);
	for($fl=1;$fl<=$n_fotosl;$fl++){
	$fotos_l[]=$a_fotosl[$fl];	
	}

	//Detalles de frentes
	$a_frentesc=explode("&",$_REQUEST['frentes_c']);
	$n_frentesc=count($a_frentesc);
	for($fc=1;$fc<=$n_frentesc;$fc++){
	$frentes_c[]=$a_frentesc[$fc];	
	}
	
	$a_frentest=explode("&",$_REQUEST['frentes_t']);
	$n_frentest=count($a_frentest);
	for($ft=1;$ft<=$n_frentest;$ft++){
	$frentes_t[]=$a_frentest[$ft];	
	}
	
	$a_frentesl=explode("&",$_REQUEST['frentes_l']);
	$n_frentesl=count($a_frentesl);
	for($fl=1;$fl<=$n_frentesl;$fl++){
	$frentes_l[]=$a_frentesl[$fl];	
	}
	
	
	//Detalles de Inventarios
	$a_inventc=explode("&",$_REQUEST['invent_c']);
	$n_inventc=count($a_inventc);
	for($ic=1;$ic<=$n_inventc;$ic++){
	$invent_c[]=$a_inventc[$ic];	
	}
	
	$a_inventt=explode("&",$_REQUEST['invent_t']);
	$n_inventt=count($a_inventt);
	for($it=1;$it<=$n_inventt;$it++){
	$invent_t[]=$a_inventt[$it];	
	}
	
	$a_inventl=explode("&",$_REQUEST['invent_l']);
	$n_inventl=count($a_inventl);
	for($il=1;$il<=$n_inventl;$il++){
	$invent_l[]=$a_inventl[$il];	
	}
	
	
	//Detalles de Inteligencia M
	$a_imc=explode("&",$_REQUEST['im_c']);
	$n_imc=count($a_imc);
	for($ic=1;$ic<=$n_imc;$ic++){
	$im_c[]=$a_imc[$ic];	
	}
	
	$a_imt=explode("&",$_REQUEST['im_t']);
	$n_imt=count($a_imt);
	for($it=1;$it<=$n_imt;$it++){
	$im_t[]=$a_imt[$it];	
	}
	
	$a_iml=explode("&",$_REQUEST['im_l']);
	$n_iml=count($a_iml);
	for($il=1;$il<=$n_iml;$il++){
	$im_l[]=$a_iml[$il];	
	}

    $manager = new bdManager();

	$sql_promo="Select r.idPromotor,COUNT(r.idTienda) as num_tiend
	from maestroTiendas t 
	inner join rutasPromotores r on (t.idTienda=r.idTienda) where r.idPromotor!=0 and r.idPromotor NOT IN ('364','367','368','369','371') and r.rol like '%AUT%'";
	
    if(isset($_REQUEST['Estado']) && !empty($Estado) ){
 
        $sql_promo.=" and t.estado='".$Estado."'";

    }


    if(isset($_REQUES['nombreTienda']) && !empty($idTienda)){

        $sql_promo.= " and t.idTienda=".$idTienda." ";

    }
	
	$sql_promo.=" GROUP BY r.idPromotor ORDER BY r.idPromotor ASC ";

	
	$rs_promo=$manager->ejecutarConsulta($sql_promo);
	
	
	///// **********************************Ciclo promotores
	while($dato_promo=mysqli_fetch_array($rs_promo))
	{

	   $sql = "Select t.grupo,t.numeroEconomico,t.cadena, t.sucursal,r.*,(lunes+martes+miercoles+jueves+viernes+sabado+domingo) as tot_visit 
		from maestroTiendas t 
		inner join rutasPromotores r on (t.idTienda=r.idTienda) where r.idPromotor='".$dato_promo['idPromotor']."' and r.rol like '%AUT%'";
	
	
	
		if(isset($_REQUEST['Estado']) && !empty($Estado) ){
	 
			$sql.=" and t.estado='".$Estado."'";
	
		}
	
		if(isset($_REQUES['nombreTienda']) && !empty($idTienda)){
	
			$sql.= " and t.idTienda=".$idTienda." ";
	
		}
	
	
		$sql.=" order by t.idTienda ASC";
	
	
	
		$i = 0;
	
		$rs = $manager->ejecutarConsulta($sql);
		echo '<table width="100%" border="1" bordercolor="#D8D8D8">
		<tr>
		<td colspan="9">
		<table width="100%"  border="1" bordercolor="#D8D8D8" >
		<tr bgcolor="#FF6700">
		<th>
			 <span>idCel</span>		
			</th>
			<th>
			 <span>Supervisor</span>		
			</th>
			<th>
			 <span>Nombre Promotor</span>		
			</th>
			</tr>';
		$a_promotor="select nombre ,Supervisor
			from Promotores where idCelular='".$dato_promo['idPromotor']."'";
			$b_promotor=$manager->ejecutarConsulta($a_promotor);
			$c_promotor=mysqli_fetch_array($b_promotor);
			
			$a_super="select  concat(nombreSupervisor,' ',apellidoSupervisor) as nom_super from Supervisores 
			where idSupervisores='".$c_promotor['Supervisor']."'";
			$b_super=$manager->ejecutarConsulta($a_super);
			
			$c_super=mysqli_fetch_array($b_super);
			echo '<tr>
			<td>
			 <span>'.$dato_promo['idPromotor'].'</span>		
			</td>
			<td>
			 <span>'.$c_super['nom_super'].'</span>		
			</td>
			<td>
			 <span>'.$c_promotor['nombre'].'</span>		
			</td>
			</tr>
			</table
			</td>
			</tr>';
		$total_prom_visit=0;
		$total_prom_fil=0;
		while($datos = mysqli_fetch_array($rs))
		{
				
			
			echo '<tr bgcolor="#9D9D9D">
			<th>
			 <span>IdTienda</span>		
			</th>
			
			<th>
			 <span>Lun</span>		
			</th>
			<th>
			 <span>Mar</span>		
			</th>
			<th>
			 <span>Mier</span>		
			</th>
			<th>
			 <span>Jue</span>		
			</th>
			<th>
			 <span>Vier</span>		
			</th>
			<th>
			 <span>Sab</span>		
			</th>
			<th>
			 <span>Tienda</span>		
			</th>
			<th>
			 <span>Marca</span>		
			</th>
		</tr>
		';
			
			
			echo '<tr>
			<td>
			 <span>'.$datos['idTienda'].'</span>		
			</td>
			<td '; 
			if($datos['lunes']==1)
			 {
				 echo 'bgcolor="#04F928"';
				 }
			echo '>			
			</td>
			<td ';
			if($datos['martes']==1)
			 {
				  echo 'bgcolor="#04F928"';
				 }
			echo '>	
			</td>
			<td ';
			if($datos['miercoles']==1)
			 {
				  echo 'bgcolor="#04F928"';
				 }
			echo '>		
			</td>
			<td ';
			if($datos['jueves']==1)
			 {
				 echo 'bgcolor="#04F928"';
				 }
			echo '>		
			</td>
			<td ';
			if($datos['viernes']==1)
			 {
				 echo 'bgcolor="#04F928"';
				 }
			echo '>	
			</td>
			<td ';
			if($datos['sabado']==1)
			 {
				  echo 'bgcolor="#04F928"';
				 }
			echo '>	
			</td>
			<td>
			 <span>'.$datos['sucursal'].'</span>		
			</td>
			<td>
			 <span>'.$datos['rol'].'</span>		
			</td>
			</tr>';
			
			////*********************Consulta las visitas realizadas en el mes
			$query2="Select * from  tiendasVisitadas tv inner join Promotores p on (tv.idCelular=p.idCelular) 
			where tv.idTienda='".$datos['idTienda']."' and tv.fecha like '%".$Mes."-".$Anio."%' group by tv.idTienda,tv.idCelular,tv.fecha order by tv.fecha ASC ";
			
			$result2=$manager->ejecutarConsulta($query2); 
			
			$query_fechas="Select MAX(tv.fecha) as hasta,MIN(tv.fecha) as desde from  tiendasVisitadas tv inner join Promotores p on (tv.idCelular=p.idCelular) 
			where tv.idTienda='".$datos['idTienda']."' and tv.fecha like '%".$Mes."-".$Anio."%' order by tv.fecha ASC ";
			$result_fechas=$manager->ejecutarConsulta($query_fechas);
			
			$visitas_fecha=mysqli_fetch_array($result_fechas);
			
			////////****************************Revisar las Visitas y contarlas
			$semana=0;
			$_SESSION[$datos['idTienda']][dia_ant]=0;
			while($visitas_a=mysqli_fetch_array($result2))
			{
				$fecha=explode("-",$visitas_a['fecha']);
				
				$num_dia=$manager->diaSemana($fecha[0],$fecha[1],$fecha[2]);
				
				
				$dia=$manager->diaNombre($num_dia);	
				
					//*****************Si ya termino la semana se aumenta el contador de semanas
					if(($_SESSION[$datos['idTienda']][dia_ant]>=$num_dia) && ($semana>0))
					{
						$semana++;
						$_SESSION[$datos['idTienda']][$semana]=0;
						}
					///******************Se pone en 1 , de semana con el dia inicial 
					if($visitas_fecha['desde']==$visitas_a['fecha'])
					{
						$semana=1;
						$_SESSION[$datos['idTienda']][$semana]=0;
						}
							
					//$_SESSION[$datos['idTienda']][$semana][$dia]=1;	
					$_SESSION[$datos['idTienda']][$semana]=$_SESSION[$datos['idTienda']][$semana]+1;
					
					///******************Si es la ultima visita , calculamos el numero de semanas
					if($visitas_fecha['hasta']==$visitas_a['fecha'])
					{
						$desde="01-".$Mes."-".$Anio;
						$hasta=date("t",$Mes)."-".$Mes."-".$Anio;
						
						$num_semanas=$manager->calculaSemanas($desde,$hasta);
						
						if($num_semanas>$semana)
						{
							$_SESSION[$datos['idTienda']][semanas]=$num_semanas;	
							}
						else
						{
							$_SESSION[$datos['idTienda']][semanas]=$semana;
						}
					}
					
					$_SESSION[$datos['idTienda']][dia_ant]=$num_dia;		
				
			}
	////****************************Final del while recorrido de visitas*********************************
			
	////*********************Recorrer las marcas para tomar datos. ********************************//
			for($i=0;$i<$num_det;$i++)
			{
				//Si se eligio marca
			   if($idMarcas[$i]>0)
			   {
				///*****************************Query Fotos**********************************.
				$fotos_a="Select * from photoCatalogo 
				where id_tienda='".$datos['idTienda']."' and id_marca='".$idMarcas[$i]."' and
				ano='$Anio' and mes='$Mes' group by id_tienda,id_promotor,id_marca,fecha order by fecha ASC";
				
				$fotos_b=$manager->ejecutarConsulta($fotos_a);

                    $fotos_query="Select MAX(fecha) as hasta,MIN(fecha) as desde from photoCatalogo 
				where id_tienda='".$datos['idTienda']."' and id_marca='".$idMarcas[$i]."' and
				ano='$Anio' and mes='$Mes'";

                    $fotos_result=$manager->ejecutarConsulta($fotos_query);
                    $fotos_fechas=mysqli_fetch_array($fotos_result);
                    $_SESSION[$datos['idTienda']][fotos][dia_ant]=0;
                    while($c_fotos=mysqli_fetch_array($fotos_b))
                    {
					$fecha=explode("-",$c_fotos['fecha']);
				
					$num_dia=$manager->diaSemana($fecha[0],$fecha[1],$fecha[2]);
					
					//*****************Si ya termino la semana se aumenta el contador de semanas
					if(($_SESSION[$datos['idTienda']][fotos][dia_ant]>=$num_dia) && ($semana>0))
					{
						$semana++;
						$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][$semana]=0;
						}
					///******************Se pone en 1 , de semana con el dia inicial 
					if($fotos_fechas['desde']==$c_fotos['fecha'])
					{
						$semana=1;
						$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][$semana]=0;
						}
						
					$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][$semana]+=1;
					
					
					
					///******************Si es la ultima visita , calculamos el numero de semanas
					if($fotos_fechas['hasta']==$c_fotos['fecha'])
					{
						
						$desde="01-".$Mes."-".$Anio;
						$hasta=date("t",$Mes)."-".$Mes."-".$Anio;
						
						$num_semanas=$manager->calculaSemanas($desde,$hasta);
						if($num_semanas>$semana)
						{
						  $_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][semanas]=$num_semanas;
						}
						else
						{
							 $_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][semanas]=$semana;
							}
					}
					$_SESSION[$datos['idTienda']][fotos][dia_ant]=$num_dia;
				  }
				 //****************************Final Fotos ********************************
				
				//***********************************Query Frentes***************************************//
				$frentes_a="Select * from frentesCharola 
				where idTienda='".$datos['idTienda']."' and fecha like '%$Mes-$Anio%' and idMarca='".$idMarcas[$i]."'
				group by idTienda,idCelular,fecha,idMarca order by fecha ASC";
				
				$frentes_b=$manager->ejecutarConsulta($frentes_a);
				
				$frentes_query="Select MAX(fecha) as hasta,MIN(fecha) as desde from frentesCharola 
				where idTienda='".$datos['idTienda']."' and fecha like '%$Mes-$Anio%' and idMarca='".$idMarcas[$i]."'";
				
				$frentes_result=$manager->ejecutarConsulta($frentes_query);
				
				$frentes_fechas=mysqli_fetch_array($frentes_result);
				
				$_SESSION[$datos['idTienda']][frentes][dia_ant]=0;
				while($c_frentes=mysqli_fetch_array($frentes_b))
				{
					$fecha=explode("-",$c_frentes['fecha']);
				
					$num_dia=$manager->diaSemana($fecha[0],$fecha[1],$fecha[2]);
					
					//*****************Si ya termino la semana se aumenta el contador de semanas
					if(($_SESSION[$datos['idTienda']][frentes][dia_ant]>=$num_dia) && ($semana>0))
					{
						$semana++;
						$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][$semana]=0;
						}
					///******************Se pone en 1 , de semana con el dia inicial 
					if($frentes_fechas['desde']==$c_frentes['fecha'])
					{
						$semana=1;
						$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][$semana]=0;
						}
						
					$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][$semana]+=1;
					
					///******************Si es la ultima visita , calculamos el numero de semanas
					if($frentes_fechas['hasta']==$c_frentes['fecha'])
					{
						$desde="01-".$Mes."-".$Anio;
						$hasta=date("t",$Mes)."-".$Mes."-".$Anio;
						
						$num_semanas=$manager->calculaSemanas($desde,$hasta);
						
						if($num_semanas>$semana)
						{
						  $_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][semanas]=$num_semanas;
						}
						else
						{
							$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][semanas]=$semana;
							}
					}
					$_SESSION[$datos['idTienda']][frentes][dia_ant]=$num_dia;
				  }////Final Frentes
			
					
			////************************************Query Inventarios***************************
				$a_invent="Select i.*,p.idMarca 
				from inventarioBodega i 
				inner join Producto p on (i.idProducto=p.idProducto) 
				where i.idTienda='".$datos['idTienda']."' and i.fecha like '%$Mes-$Anio%' and p.idMarca='".$idMarcas[$i]."'
				group by i.idTienda,p.idMarca,i.fecha,i.idPromotor order by i.fecha ASC";
				
				$b_invent=$manager->ejecutarConsulta($a_invent);
				
				$invent_query="Select MAX(i.fecha) as hasta,MIN(i.fecha) as desde 
				from inventarioBodega i 
				inner join Producto p on (i.idProducto=p.idProducto) 
				where i.idTienda='".$datos['idTienda']."' and i.fecha like '%$Mes-$Anio%' and p.idMarca='".$idMarcas[$i]."'";
				
				$invent_result=$manager->ejecutarConsulta($invent_query);
				
				$invent_fechas=mysqli_fetch_array($invent_result);
				
				$_SESSION[$datos['idTienda']][invent][dia_ant]=0;
				while($c_invent=mysqli_fetch_array($b_invent))
				{
					$fecha=explode("-",$c_invent['fecha']);
				
					$num_dia=$manager->diaSemana($fecha[0],$fecha[1],$fecha[2]);
					
					//*****************Si ya termino la semana se aumenta el contador de semanas
					if(($_SESSION[$datos['idTienda']][invent][dia_ant]>=$num_dia) && ($semana>0))
					{
						$semana++;
						$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][$semana]=0;
						}
					///******************Se pone en 1 , de semana con el dia inicial 
					if($invent_fechas['desde']==$c_invent['fecha'])
					{
						$semana=1;
						$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][$semana]=0;
						}
						
					$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][$semana]+=1;
					
					///******************Si es la ultima visita , calculamos el numero de semanas
					if($invent_fechas['hasta']==$c_invent['fecha'])
					{
						
						$desde="01-".$Mes."-".$Anio;
						$hasta=date("t",$Mes)."-".$Mes."-".$Anio;
						
						$num_semanas=$manager->calculaSemanas($desde,$hasta);
						
						if($num_semanas>$semana)
						{
						  $_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][semanas]=$num_semanas;
						}
						else
						{
							$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][semanas]=$semana;
							}
					}
					
					$_SESSION[$datos['idTienda']][invent][dia_ant]=$num_dia;
				  }///Final Inventarios
				
				
				// Si no se van a tomar en cuenta inteligencia m para esta marca
				
				
				if($im_t[$i]=="0" || $im_l[$i]=="0" || $im_c[$i]=="0")
				{
					$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][no_tomar]=1;
					
					}	
				else
				{
					
					$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][no_tomar]=0;
					//**************************************Query Inteligencia M***********************************
					 $a_im="Select im.*,p.idMarca,t.idFormato
					 from inteligenciaMercado im 
					inner join Producto p on (p.idProducto=im.idProducto)
					inner join maestroTiendas t on (t.idTienda=im.idTienda)
					where im.idTienda='".$datos['idTienda']."' and im.fecha like '%$Mes-$Anio%' and p.idMarca='".$idMarcas[$i]."'
					group by im.idPromotor,im.idTienda,im.fecha,p.idMarca order by im.fecha,t.idFormato ASC";
					$b_im=$manager->ejecutarConsulta($a_im);
					
					$im_query="Select MAX(im.fecha) as hasta,MIN(im.fecha) as desde
					 from inteligenciaMercado im 
					inner join Producto p on (p.idProducto=im.idProducto)
					where im.idTienda='".$datos['idTienda']."' and im.fecha like '%$Mes-$Anio%' and p.idMarca='".$idMarcas[$i]."'";
					
					$im_result=$manager->ejecutarConsulta($im_query);
					
					$im_fechas=mysqli_fetch_array($im_result);
					
					
					while($c_im=mysqli_fetch_array($b_im))
					{
						
						echo $im_revisa="select count(p.idProducto) as prods_tot from inteligenciaMercado im
						inner join Producto p on (p.idProducto=im.idProducto)
						where im.fecha ='".$c_im['fecha']."' and p.idMarca='".$idMarcas[$i]."' ";
						$im_revisa1=$manager->ejecutarConsulta($im_revisa);
						$im_revisa2=mysqli_fetch_array($im_revisa1);
						
						$fecha=explode("-",$c_im['fecha']);
				
						$num_dia=$manager->diaSemana($fecha[0],$fecha[1],$fecha[2]);
						
						//*****************Si ya termino la semana se aumenta el contador de semanas	
						if(($num_dia==6) && ($semana>0))
						{
							$semana++;
							}
						///******************Se pone en 1 , de semana con el dia inicial 
						if($im_fechas['desde']==$c_im['fecha'])
						{
							$semana=1;
							}
							
						$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][$semana]+=1;
						$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][prods][$semana]+=$im_revisa2['prods_tot'];
						
						
						///******************Si es la ultima visita , calculamos el numero de semanas
						if($im_fechas['hasta']==$c_im['fecha'])
						{
							$desde="01-".$Mes."-".$Anio;
							$hasta=date("t",$Mes)."-".$Mes."-".$Anio;
						
							$num_semanas=$manager->calculaSemanas($desde,$hasta);
							
							if($num_semanas>$semana)
							{
							  $_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][semanas]=$num_semanas;
							}
							else
							{
								$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][semanas]=$semana;
								}
							$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][formato]=$c_im['idFormato'];
						}
						
					} ////Final Inteligencia M
				}// Final Si se tomara Inteligencia M
			}//Final si eligieron Marca
		}
		//*****************************Final Marcas***********************************
			
	////******************************Revisa Visitas Sucursales **************************
			$total_visitas=0;
			$total_reales=0;
			$_SESSION[$datos['idTienda']][semanas];
			for($semana=1;$semana<=$_SESSION[$datos['idTienda']][semanas];$semana++)
			{
				
				////*********Si en la semana visito las veces q debia
				if(($datos['tot_visit']-1)<=$_SESSION[$datos['idTienda']][$semana])
				{
					$_SESSION[$datos['idTienda']][cumple][$semana]=1;
					$_SESSION[$datos['idTienda']][$semana]=$datos['tot_visit'];
					
					$total_visitas+=$datos['tot_visit'];
					$total_reales+=$_SESSION[$datos['idTienda']][$semana];
					
					}
				//Si son menos dias	
				else
				{
					//Revisa que no sea la primera semana y si es asi, revisa que no sea semana corta.
					if($semana==1)
					{
						$dia_uno=$manager->diaSemana("01",$Mes,$Anio);
						$tot_sem=0;
						for($j=$dia_uno;$j<=6;$j++)
						{
							$nom_dia=$manager->diaNombre($j);
							if($datos[$nom_dia]==1)
							{
								$tot_sem++;
								}
						}
						if($tot_sem==$_SESSION[$datos['idTienda']][$semana])
						{
							$_SESSION[$datos['idTienda']][cumple][$semana]=1;
		
							
						}
						else
						{
							$_SESSION[$datos['idTienda']][cumple][$semana]=0;
							}
						
						$total_visitas+=$tot_sem;
						$total_reales+=$_SESSION[$datos['idTienda']][$semana];
					}
					//Revisa que no sea la ultima semana y si es asi, revisa que no sea semana corta.
					else if($semana==5)
					{
						$dias_mes = date("t",$Mes);
						
						$ultimo_dia=$manager->diaSemana($dia_mes,$Mes,$Anio);	
						$tot_sem=0;
						for($j=$ultimo_dia;$j>=0;$j--)
						{
							$nom_dia=$manager->diaNombre($j);
							if($datos[$nom_dia]==1)
							{
								$tot_sem++;
								}
							
							}
						if($tot_sem==$_SESSION[$datos['idTienda']][$semana])
						{
							$_SESSION[$datos['idTienda']][cumple][$semana]=1;
						}
						else
						{
							$_SESSION[$datos['idTienda']][cumple][$semana]=0;
							}
						$total_visitas+=$tot_sem;
						$total_reales+=$_SESSION[$datos['idTienda']][$semana];
					}
					else if($Mes=="07") ///Cuando es mes de Julio
					{
						$tot_sem=1;
						if($tot_sem<=$_SESSION[$datos['idTienda']][$semana])
						{
							$_SESSION[$datos['idTienda']][cumple][$semana]=1;
						}
						else
						{
							$_SESSION[$datos['idTienda']][cumple][$semana]=0;
							}
						$total_visitas+=$tot_sem;
						$total_reales+=$_SESSION[$datos['idTienda']][$semana];
						
					}//Regla para el mes de Julio
					else
					{
						$_SESSION[$datos['idTienda']][cumple][$semana]=0;
						
						$total_visitas+=$datos['tot_visit'];
						
						$total_reales+=$_SESSION[$datos['idTienda']][$semana];
						}
					
				}
			}
	// ****************************************Final Revisa Visitas	**********************************
			// Totales Visitas......
			$_SESSION[$datos['idTienda']][tot_visitas]=$total_visitas;
			$_SESSION[$datos['idTienda']][tot_real]=$total_reales;
			
	////******************************Revisa  Filtros ******************************************************
			for($i=0;$i<$num_det;$i++)
			{
				// Si seleccionaron Marca
				if($idMarcas[$i]>0)
				{
				 // Revisa Fotos
				$semanas_fot=$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][semanas];
				for($j=1;$j<=$semanas_fot;$j++)
				{
					//Si piden fotos Semanales
					if($fotos_t[$i]=="SEM" && ($_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][$j]>$fotos_c[$i]))
					{	
						$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][cumple][$semana]=1;
						}
					//Si piden fotos Quincenales
					else if($fotos_t[$i]=="QUI" && ($_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][$j]>$fotos_c[$i]))
					{
						// Si es quincena 1
						if($j<3)
						{
							$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][quincena1]=1;		
							}
						//Si es quincena 2
						else 
						{
							$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][quincena2]=1;		
							}
						
					}
					////Si no aplica el filtro para esta marca	
					else if($fotos_t[$i]=="0" && $fotos_c[$i]=="0")
					{
						$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][cumple][$j]=-1;
						}
					////Si no cumple ninguna 	
					else
					{
						$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][cumple][$j]=0;
						}
					
				}/// Final Revisa Fotos
					
				 // Revisa Frentes
				$semanas_fren=$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][semanas];
				for($j=1;$j<=$semanas_fren;$j++)
				{
					//Si piden frentes Semanales
					if($frentes_t[$i]=="SEM" && ($_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][$semana]>$frentes_c[$i]))
					{	
						$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][cumple][$j]=1;
						}
					//Si piden frentes Quincenales
					else if($frentes_t[$i]=="QUI" && ($_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][$j]>$frentes_c[$i]))
					{
						// Si es quincena 1
						if($j<3)
						{
							$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][quincena1]=1;		
							}
						//Si es quincena 2
						else 
						{
							$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][quincena2]=1;		
							}
						
					}
					////Si no aplica el filtro para esta marca	
					else if($frentes_t[$i]=="0" && $frentes_c[$i]=="0")
					{
						$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][cumple][$j]=-1;
						}
					////Si no cumple ninguna 	
					else
					{
						$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][cumple][$j]=0;
						}
					
				}/// Final Revisa Frentes
				
				 // Revisa Inventarios
				$semanas_invent=$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][semanas];
				for($j=1;$j<=$semanas_invent;$j++)
				{
					//Si piden inventarios Semanales
					if($invent_t[$i]=="SEM" && ($_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][$j]>$invent_c[$i]))

					{	
						$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][cumple][$j]=1;
						}
					//Si piden inventarios Quincenales
					else if($invent_t[$i]=="QUI" && ($_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][$j]>$invent_c[$i]))
					{
						// Si es quincena 1
						if($j<3)
						{
							$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][quincena1]=1;		
							}
						//Si es quincena 2
						else 
						{
							$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][quincena2]=1;		
							}
						
					}
					////Si no aplica el filtro para esta marca	
					else if($invent_t[$i]=="0" && $invent_c[$i]=="0")
					{
						$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][cumple][$j]=-1;
						}
					////Si no cumple ninguna 	
					else
					{
						$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][cumple][$j]=0;
					}
					
					
				}/// Final Revisa Inventarios



				// Revisa IM
				/*$semanas_invent=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][semanas];
				for($j=1;$j<=$semanas_invent;$j++)
				{
					//Si piden im Semanales
					if($im_t[$i]=="SEM" && ($_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][$j]>$im_c[$i]) && ($_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][$j][prods]>10))
					{	
						$_SESSION[$datos['idPromotor']][$idMarcas[$i]][formato_cumple][]=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][formato];
						$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][cumple][$j]=1;
						}
					//Si piden im Quincenales	
					else if($im_t[$i]=="QUI" && ($_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][$j]>$im_c[$i])  && ($_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][$j][prods]>10))
					{
						// Si es quincena 1
						if($j<3)
						{
							
							$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][quincena1]=1;		
							}
						//Si es quincena 2
						else 
						{
							$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][quincena2]=1;		
							}
						if($_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][quincena2]==1 && $_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][quincena1]==1)
						{
							$_SESSION[$datos['idPromotor']][$idMarcas[$i]][formato_cumple][]=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][formato];
							}
					}
					////Si no aplica el filtro para esta marca	
					else if($im_t[$i]=="0" && $im_c[$i]=="0")
					{
						$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][cumple][$j]=-1;
						}
					////Si no cumple ninguna 	
					else
					{
						$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][cumple][$j]=0;
					}
					
					
				} */ /// Final Revisa IM
					
			}// Final Si seleccionaron Marca
		 }///Final revisa Filtros
		
	echo '<tr>
			<th bgcolor="#9D9D9D">
			<span>SEMANAS</span>
			</th>
			<th>
			<span>1</span>
			</th>
			<th>
			<span>2</span>
			</th>
			<th>
			<span>3</span>
			</th>
			<th>
			<span>4</span>
			</th>
			<th>
			<span>5</span>
			</th>
			<th>
			<span>Total Visitas</span>
			</th>
			<th>
			<span>Total Realizadas</span>
			</th>
		</tr>';
		
	echo '<tr>
			<td bgcolor="#9D9D9D">
			<span>VISITAS</span>
			</td>
			<td>
			<span>'.$_SESSION[$datos['idTienda']][1].'</span>
			</td>
			<td>
			<span>'.$_SESSION[$datos['idTienda']][2].'</span>
			</td>
			<td>
			<span>'.$_SESSION[$datos['idTienda']][3].'</span>
			</td>
			<td>
			<span>'.$_SESSION[$datos['idTienda']][4].'</span>
			</td>
			<td>
			<span>'.$_SESSION[$datos['idTienda']][5].'</span>
			</td>
			<td>
			<span>'.$total_visitas.'</span>
			</td>
			<td>
			<span>'.$total_reales.'</span>
			</td>
	</tr>
	<tr>
		<td colspan="7">
		<span>Promedio de Visitas</span>
		</td>';
		if($_SESSION[$datos['idTienda']][tot_visitas]>0)
		{
		$promedio_visit=round(($_SESSION[$datos['idTienda']][tot_real]/$_SESSION[$datos['idTienda']][tot_visitas]),4);
		}
		else
		{
			$promedio_visit=0;
			}
		echo '<td ';
		if($promedio_visit>0.5)
		{
			echo 'bgcolor="#04F928"';
			}
		else
		{
			echo 'bgcolor="#FF0000"';
			}
		echo '>
		<span>';
		if($promedio_visit>1)
		{
			$promedio_visit=1;
			}
		$total_prom_visit+=$promedio_visit;
		echo ($promedio_visit*100).' %</span>
		</td>
	</tr>
		';
		///***************************Imprime Detalles
		for($i=0;$i<$num_det;$i++)
		{
			// Si eligieron Marca
		   if($idMarcas[$i]>0)
		   {
			$a_marca="select nombre from Marca where idMarca='".$idMarcas[$i]."'";
			$b_marca=$manager->ejecutarConsulta($a_marca);
			$c_marca=mysqli_fetch_array($b_marca);
			
			echo '<tr>
			<td colspan="4" align="center">
			<span>Marca: '.$c_marca['nombre'].'</span>
			</td>
			</tr>
			<tr bgcolor="#9D9D9D">
				<th>
				</th>
				<th>
				<span>Fotos</span>
				</th>
				<th>
				<span>Frentes</span>
				</th>
				<th>
				<span>Inventarios</span>
				</th>
				<th>
				<span>Inteligencia M</span>
				</th>
			</tr>
			<tr>
			<td>
			</td>';
			
			//Imprimo datos de las fotos por marca
			$semanas_fot=$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][semanas];
			$fot_sem=0;
			$fot_qui=0;
			
			for($j=1;$j<=$semanas_fot;$j++)
			{
				if($fotos_t[$i]=="SEM")
				{	
					$fot_sem+=$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][cumple][$j];
				}
				if($fotos_t[$i]=="QUI" &&($j<3))
				{
					$fot_qui+=$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][quincena1];
					}
					else
					{
						$fot_qui+=$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][quincena2];
						}
				
			}
			echo '<td>';
			if($fot_qui==2 || ($fot_sem==$semanas_fot && $fot_sem!=0))
			{
					$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][porcentaje]=100;
				echo '100%';
				}
			else if($fot_qui<0 && $fot_sem<0)
			{
				$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][porcentaje]=-1;
				echo '-';
				}
			else
			{
				$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][porcentaje]=0;
				echo '0%';
				}
			echo '</td>';///Finaliza imprime fotos
			
			//Imprimo datos de los frentes por marca
			$semanas_fren=$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][semanas];
			$fren_sem=0;
			$fren_qui=0;
			
			for($j=1;$j<=$semanas_fren;$j++)
			{
				if($frentes_t[$i]=="SEM")
				{	
					$fren_sem+=$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][cumple][$j];
				}
				if($frentes_t[$i]=="QUI" &&($j<3))
				{
					$fren_qui+=$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][quincena1];
					}
					else
					{
						$fren_qui+=$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][quincena2];
						}
				
			}
			echo '<td>';
			if($fren_qui==2 || ($fren_sem==$semanas_fren && $fren_sem!=0))
			{
					$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][porcentaje]=100;
				echo '100%';
				}
			else if($fren_qui<0 && $fren_sem<0)
			{
				$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][porcentaje]=-1;
				echo '-';
				}
			else
			{
				$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][porcentaje]=0;
				echo '0%';
				}
			echo '</td>'; ///Final imprime frentes
			
			//*****************Imprimo datos de los inventarios por marca
			$semanas_inv=$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][semanas];
			$inv_sem=0;
			$inv_qui=0;
			
			for($j=1;$j<=$semanas_inv;$j++)
			{
				if($invent_t[$i]=="SEM")
				{	
					$inv_sem+=$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][cumple][$j];
				}
				if($invent_t[$i]=="QUI" &&($j<3))
				{
					$inv_qui+=$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][quincena1];
					}
					else
					{
						$inv_qui+=$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][quincena2];
						}
				
			}
			echo '<td>';
			if($inv_qui==2 || ($inv_sem==$semanas_inv && $inv_sem!=0))
			{
					$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][porcentaje]=100;
				echo '100%';
				}
			else if($inv_qui<0 && $inv_sem<0)
			{
				$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][porcentaje]=-1;
				echo '-';
				}
			else
			{
				$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][porcentaje]=0;
				echo '0%';
				}
			echo '</td>'; ///Final imprime inventarios
			
			//*****************Imprimo datos de los inteligencia m por marca
		$semanas_im=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][semanas];
			$im_sem=0;
			$im_qui=0;
			
			for($j=1;$j<=$semanas_im;$j++)
			{
				if($im_t[$i]=="SEM")
				{	
					$im_sem+=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][cumple][$j];
				}
				if($im_t[$i]=="QUI" &&($j<3))
				{
						$im_qui+=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][quincena1];
					}
					else
					{
						$im_qui+=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][quincena2];
						}
				
			}
			echo '<td>';
			if($im_qui==2 || ($im_sem==$semanas_im && $im_sem!=0))
			{
					$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][porcentaje]=100;
				echo '100%';
				}
			else if($im_qui<=0 && $im_sem<=0 && $im_c[$i]==0)
			{
				$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][porcentaje]=-1;
				echo '-';
				}
			else
			{
				$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][porcentaje]=0;
				echo '0%';
				}
			echo '</td>'; ///Final imprime inteligencia m
			
			
			echo '</tr>';
		   }// Final si eligieron Marca
		}///******************************Fin Imprime Detalles
		
		///***************************Imprime Totales
		$porcentaje_fot=0;
		$porcentaje_fren=0;
		$porcentaje_inv=0;
		$porcentaje_im=0;
		$marcas_fot=$num_det;
		$marcas_fren=$num_det;
		$marcas_inv=$num_det;
		$marcas_im=$num_det;
		
		for($i=0;$i<$num_det;$i++)
		{
			///Si hay marca seleccionada
		   if($idMarcas[$i]>0)
		   {
				///Totales Fotos
				
				if($_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][porcentaje]<0)
				{
					$marcas_fot-=1;
					}
				else
				{
					$porcentaje_fot+=$_SESSION[$datos['idTienda'].'fotos'][$idMarcas[$i]][porcentaje];
					}
					
				///Totales Frentes
				
				if($_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][porcentaje]<0)
				{
					$marcas_fren-=1;
					}
				else
				{
					$porcentaje_fren+=$_SESSION[$datos['idTienda'].'frentes'][$idMarcas[$i]][porcentaje];
					}
					
				///Totales Inventarios
				
				if($_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][porcentaje]<0)
				{
					$marcas_inv-=1;
					}
				else
				{
					$porcentaje_inv+=$_SESSION[$datos['idTienda'].'invent'][$idMarcas[$i]][porcentaje];
					}
					
				///Totales Inteligencia M
				
				if($_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][porcentaje]<0)
				{
					$marcas_im-=1;
					}
				else
				{
					$porcentaje_im+=$_SESSION[$datos['idTienda'].'im'][$idMarcas[$i]][porcentaje];
					}
		   } ///Final si elgio una marca
		} //*************************/Finaliza Imprime Totales
		
		/// *******Si alguno fue calculado ...
		if($porcentaje_im!=0|| $porcentaje_inv!=0 || $porcentaje_fren!=0 || $porcentaje_fot!=0)
		{
		 $n_datos=4;
		 $porcen_sum=0;
		echo '<tr>
				<td>
				<span>Totales</span>
				</td>
				<td>
				<span>';
				if($marcas_fot>1)
				{
				  $tot_fot=round(($porcentaje_fot/($marcas_fot-1)),0);
				  $porcen_sum+=$tot_fot;
				}
				else
				{
					$n_datos-=1;
					$tot_fot='-';
					}
				echo $tot_fot.' %';
				echo '</span>
				</td>
				<td>
				<span>'; 
				if($marcas_fren>1)
				{
				 $tot_fren=round(($porcentaje_fren/($marcas_fren-1)),0);
				 $porcen_sum+=$tot_fren;
				}
				else
				{
					$n_datos-=1;
					$tot_fren='-';
					}
				echo $tot_fren.' %';
				echo '</span>
				</td>
				<td>
				<span>';
				if($marcas_inv>1)
				{
				  $tot_inv=round(($porcentaje_inv/($marcas_inv-1)),0);
				  $porcen_sum+=$tot_inv;
				}
				else
				{
					$n_datos-=1;
					$tot_inv='-';
					}
				echo $tot_inv.' %';
				echo '</span>
				</td>
				<td>
				<span>';
				if($marcas_im>1)
				{
				  $tot_im=round(($porcentaje_im/($marcas_im-1)),0);
				  $porcen_sum+=$tot_im;
				}
				else
				{
					$n_datos-=1;
					$tot_im='-';
					}
				echo $tot_im.' %';
				echo '</span>
				</td>
			</tr>';
			if($n_datos>0)
			{
				$porcent_tot=$porcen_sum/$n_datos;
				}
			echo '<tr>
			<td colspan="3">
			<span>Total Filtros: </span>
			</td>
			<td>
			<span>';
			$total_prom_fil+=$porcent_tot;
			echo $porcent_tot.' %';
			echo '</span>
			</td>
			</tr>';
		} ////*********************Final Si alguno fue calculado
		
		
	
	}// Final While Tiendas
	$_SESSION[$datos['idPromotor']][cumple_im]=0;
	//Recorre las marcas para ver cuales piden IM y ver si cumplieron todos los formatos.
	$im_marcas=0;
	for($i=0;$i<$num_det;$i++)
	{
		if($im_c[$i]>0)
		{
			$im_marcas++;
			$sql_formatos="SELECT p.idCelular, t.idFormato
			FROM Promotores p
			INNER JOIN rutasPromotores r ON (p.idCelular = r.idPromotor)
			INNER JOIN maestroTiendas t ON (r.idTienda = t.idTienda)
			and p.idCelular='".$dato_promo['idPromotor']."' GROUP BY t.idFormato ORDER BY p.idCelular";
			$result_formatos=$manager->ejecutarConsulta($sql_formatos);
			$formatos_marc=0;
			$n_formato=mysqli_num_rows($result_formatos);
			while($formatos_prom=mysqli_fetch_array($result_formatos))
			{
				if(in_array($formatos_prom['idFormato'],$_SESSION[$datos['idPromotor']][$idMarcas[$i]][formato_cumple]))
				{	
					$formatos_marc++;
					}
				}

			if($n_formato==$formatos_marc)
			{
				$_SESSION[$datos['idPromotor']][cumple_im]+=1;
				}
		}
	}
	echo '<tr>
	<td>
		<table width="100%"  border="1" bordercolor="#D8D8D8">
		<tr  bgcolor="#FF6700">
		<th>
		  <span>Prom Inteligencia M</span>
		</th>
		<th>
		<span>Total Prom Visitas</span>
		</th>
		<th>
		<span>Total Prom Filt</span>
		</th>
		<th>
		<span>Promedio Total</span>
		</th>
		</tr>
		<tr>
		<td>
		<span>';
		if($_SESSION[$datos['idPromotor']][cumple_im]==$im_marcas)
		{
			$prom_im=100;
			echo '100 %';
			}
		else
		{
			$prom_im=0;
			echo '0 %';
			}
		echo '</span>
		</td>
		<td>
		<span>';
		$prom_visit=$total_prom_visit/$dato_promo['num_tiend'];
		$prom_visit=$prom_visit*100;
		echo round($prom_visit,3).' %';
		echo '</span>
		</td>
		<td>
		<span>';
		$prom_filt=$total_prom_fil/$dato_promo['num_tiend'];
		echo round($prom_filt,3).' %';
		echo '</span>
		</td>
		<td>
		<span>';
		$prom_total=($prom_visit+$prom_filt)/2;
		echo round($prom_total,3).' %';
		echo '</span>
		</td>
		</tr>
		</table>
	</td>
	</tr>';
	
	 echo '</table>';// Cierra Tabla Principal
	
}/////Final While Promotores
   


}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



