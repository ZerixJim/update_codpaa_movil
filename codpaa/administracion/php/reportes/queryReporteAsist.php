<?php

/**
 * Created by DreamW.
 * User: Christian
 * Date: 11/12/14
 * Time: 13:59
 */


ob_start();

session_start();


include_once('../../connexion/DataBase.php');


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    $manager = DataBase::getInstance();

    $idSupervisor = $_REQUEST['Supervisor'];

    $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $idSem = $_REQUEST['idSem'];

    $id_marca = $_REQUEST['idMarca'];

    $id_tipoT = $_REQUEST['idTipoTie'];


    $date = explode("-",$idSem);




    //***** calculamos las fechas de la semana
    $dias_semana = $manager->diasSemana($date[1], $date[0]);

    $filtro = "";
    if ($idSupervisor != "") {
        $filtro = " and p.Supervisor='" . $idSupervisor . "'";
    }

    $n_estado = count($id_estado);
    $k = 0;
    if ($n_estado > 0) {
        foreach ($id_estado as $estados) {
            if ($estados != "all") {
                if ($k == 0) {
                    $filtro .= " and (p.idEstado='" . $estados . "' ";
                } else {
                    $filtro .= " or p.idEstado='" . $estados . "'";
                }
                $k++;
            }
        }
        if ($k != 0) {
            $filtro .= ")";
        }
    }


    $n_marc = count($id_marca);
    $k = 0;
    if ($n_marc > 0) {
        foreach ($id_marca as $marcas) {
            if ($k == 0) {
                $filtro .= " and (mp.idMarca='" . $marcas . "' ";
            } else {
                $filtro .= " or mp.idMarca='" . $marcas . "'";
            }
            $k++;
        }
        $filtro .= ")";
    }


    echo '<div class="table-responsive-vertical shadow-z-1">
<table class="table table-hover table-mc-light-green scroll">
		<thead>
		<tr bgcolor="#000000">
				<th>
				 <span>idPromotor</span>		
				</th>
				<th>
				 <span>Supervisor</span>		
				</th>
				<th>
				 <span>NombreProm</span>		
				</th>
				<th>
				 <span>Estado</span>		
				</th>
				<th >
				 <span>IdTienda</span>		
				</th>
				
				<th >
				<span>Tienda</span>
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
				</th>';

    ////*********************Recorre las fechas **********************//

    for ($i = 0; $i <= 6; $i++) {
        echo "<th> 
				<span> " . $dias_semana[$i] . "
				</span>
				</th>";
    }

    echo '<th>
				 <span>Frentes</span>		
				</th>
				<th>
				 <span>Inventarios</span>		
				</th>
				<th>
				 <span>InteligenciaM</span>		
				</th>';

    echo '
			</tr>
			</thead>';


    //***************************************** Seleccionar Promotores*************************************************//

    $sql_promo = "SELECT * FROM Promotores p INNER JOIN marcaAsignadaPromotor mp 
	ON (p.idCelular=mp.idPromotor) WHERE p.status='a' " . $filtro . " GROUP BY p.idCelular";


    $rs_promo = $manager->ejecutarConsulta($sql_promo);


    ///// **********************************Ciclo promotores
    while ($dato_promo = mysqli_fetch_array($rs_promo)) {

        $i = 0;

        //*******Consulta datos del Promotor
        $a_promotor = "SELECT p.nombre ,p.Supervisor,e.nombre AS nomEstado
		FROM Promotores p 
		INNER JOIN estados e ON (p.idEstado=e.id)
		WHERE idCelular='" . $dato_promo['idPromotor'] . "'";
        $b_promotor = $manager->ejecutarConsulta($a_promotor);
        $c_promotor = mysqli_fetch_array($b_promotor);

        ///***********Consulta el supervisor asignado
        $a_super = "SELECT  concat(nombreSupervisor,' ',apellidoSupervisor) AS nom_super FROM Supervisores 
		WHERE idSupervisores='" . $c_promotor['Supervisor'] . "'";
        $b_super = $manager->ejecutarConsulta($a_super);

        $c_super = mysqli_fetch_array($b_super);


        //****Consulta las tiendas que ha visitado alguna vez
        $sql_tiend = "SELECT idTienda FROM supervisionRutas r  
		WHERE idPromotor='" . $dato_promo['idPromotor'] . "' AND anio='" . $dat_sem['anio'] . "' AND semana='" . $dat_sem['semana'] . "'
		GROUP BY r.idTienda,r.idPromotor
		ORDER BY r.idTienda DESC ";


        $rs_tiend = $manager->ejecutarConsulta($sql_tiend);

        $total_prom_visit = 0;
        $total_prom_fil = 0;
        //*****************************Ciclo Tiendas del Promotor *******************************//
        while ($datos_tiend = mysqli_fetch_array($rs_tiend)) {


            echo '<tr>
			<td data-title="idPromotor">
			 	<span>' . $dato_promo['idPromotor'] . '</span>		
			</td>
			<td data-title="Supervisor">
			 	<span>' . $c_super['nom_super'] . '</span>		
			</td>
			<td data-title="NombreProm">
				 <span>' . $c_promotor['nombre'] . '</span>		
			</td>
			<td data-title="Estado">
				 <span>' . utf8_encode($c_promotor['nomEstado']) . '</span>		
			</td>
			
			';

            //***Revisa si la tienda esta en la ruta actual
            $sql_ruta = "SELECT t.grupo,t.numeroEconomico,t.cadena, t.sucursal,r.*,
		   (lunes+martes+miercoles+jueves+viernes+sabado+domingo) AS tot_visit,t.x AS lat ,t.y AS lon 
			FROM maestroTiendas t 
			LEFT JOIN rutasPromotores r ON (t.idTienda=r.idTienda) 
			WHERE r.idPromotor='" . $dato_promo['idPromotor'] . "' AND t.idTienda='" . $datos_tiend['idTienda'] . "' ORDER BY t.idTienda ASC";

            $rs_ruta = $manager->ejecutarConsulta($sql_ruta);

            $dato_ruta = mysqli_fetch_array($rs_ruta);

            if ($dato_ruta['sucursal'] == NULL) {
                $sql_ruta = "SELECT t.idTienda,t.grupo,t.numeroEconomico,t.cadena, t.sucursal,t.x AS lat ,t.y AS lon 
				FROM maestroTiendas t 
				WHERE t.idTienda='" . $datos_tiend['idTienda'] . "' ORDER BY t.idTienda ASC";

                $rs_ruta = $manager->ejecutarConsulta($sql_ruta);

                $dato_ruta = mysqli_fetch_array($rs_ruta);
            }

            echo '
			<td data-title="idTienda">
			 <span>' . $dato_ruta['idTienda'] . '</span>		
			</td>
			<td data-title="Tienda">
			 <span>' . $dato_ruta['sucursal'] . '</span>		
			</td>
						
			<td data-title="Lun" ';
            if ($dato_ruta['lunes'] == 1) {
                echo 'bgcolor="#04F928"';
            }
            echo '>			
			</td>
			<td data-title="Mar" ';
            if ($dato_ruta['martes'] == 1) {
                echo 'bgcolor="#04F928"';
            }
            echo '>	
			</td>
			<td data-title="Mier" ';
            if ($dato_ruta['miercoles'] == 1) {
                echo 'bgcolor="#04F928"';
            }
            echo '>		
			</td>
			<td data-title="Jue" ';
            if ($dato_ruta['jueves'] == 1) {
                echo 'bgcolor="#04F928"';
            }
            echo '>		
			</td>
			<td data-title="Vier" ';
            if ($dato_ruta['viernes'] == 1) {
                echo 'bgcolor="#04F928"';
            }
            echo '>	
			</td>
			<td data-title="Sab" ';
            if ($dato_ruta['sabado'] == 1) {
                echo 'bgcolor="#04F928"';
            }
            echo '>	
			</td>
			
			';

            for ($i = 0; $i <= 6; $i++) {
                ////*********************Consulta las visitas en la semana
                $query2 = "SELECT tv.* FROM  tiendasVisitadas tv 
				INNER JOIN Promotores p ON (tv.idCelular=p.idCelular) 
				WHERE tv.idTienda='" . $dato_ruta['idTienda'] . "' AND tv.fecha='" . $dias_semana[$i] . "' AND tv.tipo='E'
				AND p.idCelular='" . $dato_ruta['idPromotor'] . "'
				GROUP BY tv.idTienda,tv.idCelular ORDER BY tv.fecha ASC ";

                $result2 = $manager->ejecutarConsulta($query2);

                $n_vis = mysqli_num_rows($result2);
                if ($n_vis > 0) {
                    $visitas_a = mysqli_fetch_array($result2);

                    $distancia_k = $manager->Distancia($dato_ruta['lat'], $dato_ruta['lon'], $visitas_a['latitud'], $visitas_a['longitud'], "K");

                    if ($distancia_k < 1) {
                        echo "<td data-title='" . $dias_semana[$i] . "' bgcolor='#80FF00'>	</td>";
                    } else {

                        echo "<td data-title='" . $dias_semana[$i] . "' bgcolor='#E5FF27'>	</td>";

                    }


                } else {
                    echo "<td data-title='" . $dias_semana[$i] . "' bgcolor='#BDBDBD'>	</td>";
                }

            }
            ////*****************************Final del while recorrido de visitas*********************************


            ///**************************Ciclo revision capturas

            $frentes_t = 0;
            $inventario_t = 0;
            $im_t = 0;

            $filtro1 = "";
            $filtro2 = "";

            $n_marc = count($id_marca);
            $k = 0;
            if ($n_marc > 0) {
                foreach ($id_marca as $marcas) {
                    if ($k == 0) {
                        $filtro1 .= " and (idMarca='" . $marcas . "' ";
                        $filtro2 .= " and (p.idMarca='" . $marcas . "' ";
                    } else {
                        $filtro1 .= " or idMarca='" . $marcas . "'";
                        $filtro2 .= " or p.idMarca='" . $marcas . "' ";
                    }
                    $k++;
                }
                $filtro1 .= ")";
                $filtro2 .= ")";
            }
            for ($i = 0; $i <= 6; $i++) {

                //***********************************Query Frentes***************************************//
                $frentes_a = "SELECT sum(cha1+cha2+cha3+cha4+cha5+cha6) AS frentes_tot FROM frentesCharola 
						WHERE idTienda='" . $datos_tiend['idTienda'] . "' AND fecha ='" . $dias_semana[$i] . "' 
						" . $filtro1 . " AND idCelular='" . $dato_ruta['idPromotor'] . "'";

                $frentes_b = $manager->ejecutarConsulta($frentes_a);

                $frentes_c = mysqli_fetch_array($frentes_b);

                if ($frentes_c['frentes_tot'] > 0) {
                    $frentes_t += $frentes_c['frentes_tot'];
                }

                ////************************************Query Inventarios***************************
                $a_invent = "SELECT i.*,p.idMarca 
						FROM inventarioBodega i 
						INNER JOIN Producto p ON (i.idProducto=p.idProducto) 
						WHERE i.idTienda='" . $datos_tiend['idTienda'] . "' AND i.fecha='" . $dias_semana[$i] . "' 
						" . $filtro2 . " AND i.idPromotor='" . $dato_ruta['idPromotor'] . "'
						GROUP BY i.idProducto,p.idMarca";

                $b_invent = $manager->ejecutarConsulta($a_invent);

                $n_invent = mysqli_num_rows($b_invent);

                if ($n_invent > 0) {
                    $inventario_t += $n_invent;
                }


                ///*************************************Query IM ***************************
                $a_im = "SELECT im.*,p.idMarca,t.idFormato
						 FROM inteligenciaMercado im 
						INNER JOIN Producto p ON (p.idProducto=im.idProducto)
						INNER JOIN maestroTiendas t ON (t.idTienda=im.idTienda)
						WHERE im.idTienda='" . $datos_tiend['idTienda'] . "' AND im.fecha='" . $dias_semana[$i] . "' 
						" . $filtro2 . " AND im.idPromotor='" . $dato_ruta['idPromotor'] . "'
						GROUP BY im.idProducto,p.idMarca";

                $b_im = $manager->ejecutarConsulta($a_im);

                $n_im = mysqli_num_rows($b_im);

                if ($n_im > 0) {
                    $im_t += $n_im;
                }

            }///********************Finaliza recorrido de fechas

            ////***********************Total de Frentes
            if ($frentes_t > 0) {
                $colorF = "#DDF529";

                echo "<td data-title='Frentes' bgcolor='" . $colorF . "'><a href='#' onClick='verFrentes(" . $datos_tiend['idTienda'] . "," . strtotime($_REQUEST['Desde']) . "," . strtotime($_REQUEST['Hasta']) . "," . $dato_ruta['idPromotor'] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span> " . $frentes_t . "</span></a></td>";
            } else {
                $colorF = "#FB2D30";
                echo "<td data-title='Frentes' bgcolor='" . $colorF . "'><span> " . $frentes_t . "</span></td>";
            }


            ////***********************Total de Inventarios
            if ($inventario_t > 0) {
                $colorT = "#DDF529";
                echo "<td data-title='Inventarios' bgcolor='" . $colorT . "'><a href='#' onClick='verInventarios(" . $datos_tiend['idTienda'] . "," . strtotime($_REQUEST['Desde']) . "," . strtotime($_REQUEST['Hasta']) . "," . $dato_ruta['idPromotor'] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span> " . $inventario_t . "</span></a></td>";
            } else {
                $colorT = "#FB2D30";
                echo "<td data-title='Inventarios' bgcolor='" . $colorT . "'><span> " . $inventario_t . "</span></td>";
            }


            ////***********************Total de Inteligencia M
            if ($im_t > 0) {
                $colorI = "#DDF529";
                echo "<td data-title='InteligenciaM' bgcolor='" . $colorI . "'><a href='#' onClick='verIM(" . $datos_tiend['idTienda'] . "," . strtotime($_REQUEST['Desde']) . "," . strtotime($_REQUEST['Hasta']) . "," . $dato_ruta['idPromotor'] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span> " . $im_t . "</span></a></td>";
            } else {
                $colorI = "#FB2D30";
                echo "<td data-title='InteligenciaM' bgcolor='" . $colorI . "'><span> " . $im_t . "</span></td>";
            }


            echo "</tr>";

        }

//******************************************** Final While Tiendas*******************************************************//


    }/////Final While Promotores

    echo '</table>
 </div>';// Cierra Tabla Principal

} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



