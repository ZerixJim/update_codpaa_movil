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


///*********************************Recibo los datos de los filtros *******************************//

    $manager = DataBase::getInstance();
    //id del estado
    $id_estado = $_REQUEST['idEstado'];
    //region
    $regionmx = $_REQUEST['RegionMx'];
    //numero economico
    $num_eco = $_REQUEST['NumEco'];


    $tipo_cons = $_REQUEST['tipoCons'];

    $MesLab = $_REQUEST['MesLab'];

    $GrupoM = $_REQUEST['GrupoM'];

    $noVisitadas = $_REQUEST['NoVisitadas'];

    $fecha_c = explode('-', $MesLab);

    $dias_mes = date('t', $fecha_c[0]);

    //********datos de fecha actual
    $mes_act = date('m');

    $mes_ant = intval($mes_act) - 1;

    if ($mes_ant < 10) {
        $mes_ant = "0" . $mes_ant;
    }

    $anio_act = date('Y');

    $dia_act = date('j');
    //********************************

    $mes = intval($fecha_c[0]);

    if ($mes < 10) {
        $mes = "0" . $mes;
    }
    //Fechas del mes para consultas....
    $fecha_ini = "01-" . $mes . "-" . $fecha_c[1];

    $fecha_fin = $dias_mes . "-" . $mes . "-" . $fecha_c[1];

    //Fechas del mes para consulta con date..
    $fecha_a = explode('-', $fecha_ini);

    $fecha_b = explode('-', $fecha_fin);

    $fechaInicio2 = $fecha_a[2] . "-" . $fecha_a[1] . "-" . $fecha_a[0];

    $fechaFin2 = $fecha_b[2] . "-" . $fecha_b[1] . "-" . $fecha_b[0];


    $mes_vent = intval($fecha_c[0]) - 1;

    $dias_mes_v = date('t', $mes_vent);

    if ($mes_vent < 10) {
        $mes_vent = "0" . $mes_vent;
    }
    if ($fecha_c[0] == 1) {
        $anio_vent = $fecha_c[1] - 1;
    } else {
        $anio_vent = $fecha_c[1];
    }
    //Fechas del mes para consulta de ventas...
    $fecha_ini_v = "01-" . $mes_vent . "-" . $anio_vent;
    $fecha_fin_v = $dias_mes_v . "-" . $mes_vent . "-" . $anio_vent;

    $id_marca = $_REQUEST['idMarca'];

    $filtro = "";

    if ($id_estado != "" && $id_estado != "all") {
        $filtro .= " and mT.idEstado='" . $id_estado . "'";
    }

    if ($num_eco != "") {
        $filtro .= " and mT.numeroEconomico='" . $num_eco . "'";
    }

    if ($regionmx != "" && $regionmx != "all") {

        $query_est = "SELECT * FROM estados WHERE id_region='" . $regionmx . "'";
        $rs_est = $manager->ejecutarConsulta($query_est);
        $n_est = 0;
        while ($dat_est = mysqli_fetch_array($rs_est)) {
            if ($n_est == 0) {
                $filtro .= " and (mT.idEstado='" . $dat_est['id'] . "'";
            } else {
                $filtro .= " or mT.idEstado='" . $dat_est['id'] . "'";
            }
            $n_est++;
        }

        $filtro .= ")";
    }

    if ($GrupoM != "" && $GrupoM != "TODOS") {
        $filtro .= " and mT.grupo='" . $GrupoM . "'";
    }


    $filtro_mar = "";
    $sql_tienda = "SELECT DISTINCT mT.idTienda,ctm.idPromotor 
      FROM cod_tienda_marca_promotor ctm
	  INNER JOIN maestroTiendas mT ON (mT.idTienda=ctm.idTienda)
	  WHERE mT.idTipoTienda='2' AND mes='" . $fecha_c[0] . "' AND 
	    anio='" . $fecha_c[1] . "' AND idMarca IN ( ";

    $n_marc = count($id_marca);
    $k = 0;
    if ($n_marc > 0) {
        foreach ($id_marca as $marcas) {
            if ($k == 0) {
                $sql_tienda .= " '" . $marcas . "' ";
                $filtro_mar .= " '" . $marcas . "'";
            } else {
                $sql_tienda .= " ,'" . $marcas . "'";
                $filtro_mar .= " ,'" . $marcas . "'";
            }
            $k++;
        }
        $sql_tienda .= ")";
    }
    $sql_tienda .= " " . $filtro . "
		GROUP BY ctm.idTienda
		order by mT.grupo ASC";

    $rs_tienda = $manager->ejecutarConsulta($sql_tienda);


    echo ' <div class="table-responsive-vertical shadow-z-1">
		<table class="table table-hover table-mc-light-green scroll">
		<thead>
		<tr bgcolor="#FFA726">
		<th>No</th>  
		<th>Cadena</th>
		<th>Grupo</th>
		<th>Tienda</th>
		<th>Ciudad</th>
		<th>Estado</th>
		<th>idProm</th>';

    if ($noVisitadas == 0) {
        if ($tipo_cons == 1 || $tipo_cons == 2) {

            echo '<th>Vtas_Est</th>';

        }
        if ($tipo_cons == 1 || $tipo_cons == 2) {
            echo '<th>Vtas_Ofic</th>';
        }
        if ($tipo_cons == 1 || $tipo_cons == 3) {
            echo '<th>Marketing</th>';
        }
        if ($tipo_cons == 1 || $tipo_cons == 4) {
            echo '<th>Fotos</th>';
        }
        if ($tipo_cons == 1 || $tipo_cons == 5) {
            echo '<th>Invent</th>';
        }
    }
    echo '</tr>
		</thead>
		';


    ///// *************************Ciclo Tiendas
    while ($dato_tienda = mysqli_fetch_array($rs_tienda)) {
        //Si es el mes actual.. CALCULA
        if (($mes == $mes_act && $anio_act == $fecha_c[1]) || ($mes == $mes_ant && intval($dia_act) < 10)) {
            echo "<script>console.log('Entro');</script>";
            $sql_ruta = "SELECT mT.*,
			f.cadena AS cadenaf,
			e.nombre AS estadoMex,
			l.nombre AS ciudadN
			FROM maestroTiendas mT 
			LEFT JOIN tiendas_formatos f ON (mT.idFormato = f.idFormato)
			LEFT JOIN estados e ON (e.id = mT.idEstado)
			LEFT JOIN localidades l ON (mT.municipio=l.id)
			WHERE idTienda='" . $dato_tienda['idTienda'] . "'";

            $rs_ruta = $manager->ejecutarConsulta($sql_ruta);

            $dato_ruta = mysqli_fetch_array($rs_ruta);

            $sql_vis = "SELECT COUNT(idTiendasVisitadas) AS tot_vis FROM tiendasVisitadas
			WHERE fecha LIKE '%" . $fecha_c[0] . "-" . $fecha_c[1] . "%' AND idTienda='" . $dato_tienda['idTienda'] . "' AND tipo='E'";

            $rs_vis = $manager->ejecutarConsulta($sql_vis);

            $dato_vis = mysqli_fetch_array($rs_vis);

            if ($dato_vis['tot_vis'] > 0) {
                $visitada = "bgcolor='#58FA58'";
            } else {
                $visitada = "bgcolor='#FE2E2E'";
            }

            //*************** Si el reporte es completo ***********************/
            if ($noVisitadas == 0) {
                echo '
			<tr>
			<td ' . $visitada . ' data-title="No"><span>' . $dato_ruta['numeroEconomico'] . '</span></td>
			<td data-title="Cadena"><span>' . $dato_ruta['cadenaf'] . '</span></td>
			<td data-title="Grupo"><span>' . $dato_ruta['grupo'] . '</span></td>
			<td data-title="Tienda"><span>' . htmlentities($dato_ruta['sucursal']) . '</span></td>
			<td data-title="Ciudad"><span>' . htmlentities($dato_ruta['ciudadN']) . '</span></td>
			<td data-title="Estado"><span>' . htmlentities($dato_ruta['estadoMex']) . '</span></td>
			<td data-title="idProm"><span>' . $dato_tienda['idPromotor'] . '</span></td>
			';


                //******************************Cuenta de datos **************//
                $n_marc = count($id_marca);
                $ventas_prom = 0;
                $ventas_of = 0;
                $im_tot = 0;
                $fotos_tot = 0;
                $inv_tot = 0;
                $fren_tot = 0;
                if ($n_marc > 0) {
                    //*****Si la tienda se visito, se consultan los datos
                    if ($dato_vis['tot_vis'] > 0) {
                        //*************Recorre las marcas seleccionadas
                        foreach ($id_marca as $marcas) {
                            //*************Si eligio una consulta completa
                            if ($tipo_cons == 1) {

                                $query_mayc = "CALL may_comp(" . $dato_tienda['idTienda'] . "," . $marcas . ",'" . $fecha_ini . "','" . $fecha_fin . "','" . $fecha_ini_v . "','" . $fecha_fin_v . "');";

                                $rs_mayc = $manager->ejecutarConsulta($query_mayc);

                                $dat_mayc = mysqli_fetch_array($rs_mayc);

                                if ($dat_mayc['fotos'] != NULL || $dat_mayc['fotos'] != "") {
                                    $fotos_tot += $dat_mayc['fotos'];
                                }
                                if ($dat_mayc['frentesTot'] != NULL || $dat_mayc['frentesTot'] != "") {
                                    $fren_tot += $dat_mayc['frentesTot'];
                                }
                                if ($dat_mayc['im_tot'] != NULL || $dat_mayc['im_tot'] != "") {
                                    $im_tot += $dat_mayc['im_tot'];
                                }
                                if ($dat_mayc['inv_tot'] != NULL || $dat_mayc['inv_tot'] != "") {
                                    $inv_tot += $dat_mayc['inv_tot'];
                                }
                                if ($dat_mayc['surt_tot'] != NULL || $dat_mayc['surt_tot'] != "") {
                                    $ventas_prom += $dat_mayc['surt_tot'];
                                }
                            } //***Consulta de Ventas
                            if ($tipo_cons == 2) {
                                $vtas_est = "SELECT sum(cajas) AS venta_est FROM surtidoMueble sm
							INNER JOIN Producto p ON (sm.idProducto=p.idProducto)
							WHERE (STR_TO_DATE(sm.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('" . $fecha_ini_v . "','%d-%m-%Y')
							AND STR_TO_DATE('" . $fecha_fin_v . "','%d-%m-%Y'))
							AND surtido='Si'  
							AND sm.idTienda='" . $dato_tienda['idTienda'] . "' AND p.idMarca='" . $marcas . "'";
                                $rs_vtaEs = $manager->ejecutarConsulta($vtas_est);

                                $dat_vtaEs = mysqli_fetch_array($rs_vtaEs);

                                if ($dat_vtaEs['venta_est'] > 0) {
                                    $ventas_prom += $dat_vtaEs['venta_est'];
                                }

                            }//****Consulda de IM
                            if ($tipo_cons == 3) {
                                $query_im = "SELECT count(idInteligencia) AS im_tot FROM inteligenciaMercado im
						LEFT JOIN Producto p ON (p.idProducto=im.idProducto)
						WHERE im.fecha_captura BETWEEN '" . $fechaInicio2 . "'	AND '" . $fechaFin2 . "'
						AND p.idMarca='" . $marcas . "' AND im.idTienda='" . $dato_tienda['idTienda'] . "'";
                                $rs_im = $manager->ejecutarConsulta($query_im);

                                $dat_im = mysqli_fetch_array($rs_im);

                                if ($dat_im['im_tot'] > 0) {
                                    $im_tot += $dat_im['im_tot'];
                                }

                            }
                            //*****Consulta de Fotos
                            if ($tipo_cons == 4) {
                                $query_fot = "SELECT count(idphotoCatalogo) AS fot_tot FROM photoCatalogo ph
							WHERE DATE(ph.fecha_captura) BETWEEN '" . $fechaInicio2 . "' AND '" . $fechaFin2 . "'
							AND ph.id_tienda='" . $dato_tienda['idTienda'] . "' AND ph.id_marca='" . $marcas . "'";

                                $rs_fot = $manager->ejecutarConsulta($query_fot);

                                $dat_fot = mysqli_fetch_array($rs_fot);

                                if ($dat_fot['fot_tot'] > 0) {
                                    $fotos_tot += $dat_fot['fot_tot'];
                                }

                            }//*****Consulta de Inventario
                            if ($tipo_cons == 5) {
                                $query_inv = "SELECT sum(cantidadFisico) AS inv_tot FROM inventarioBodega inv
							LEFT JOIN Producto p ON (p.idProducto=inv.idProducto)
							WHERE inv.fecha_captura BETWEEN '" . $fechaInicio2 . "' AND '" . $fechaFin2 . "' AND inv.tipo='Cajas'
							AND p.idMarca='" . $marcas . "' AND inv.idTienda='" . $dato_tienda['idTienda'] . "'";
                                $rs_inv = $manager->ejecutarConsulta($query_inv);

                                $dat_inv = mysqli_fetch_array($rs_inv);

                                if ($dat_inv['inv_tot'] > 0) {
                                    $inv_tot += $dat_inv['inv_tot'];
                                }

                            }


                        }

                    }// ***** Final del if de visitas
                }//**** Final del If de marcas

                //*********************************** Totales de Datos ********************************
                // Ventas
                if ($tipo_cons == 1 || $tipo_cons == 2) {
                    //Ventas Promedio (Surtido Mueble)
                    if ($ventas_prom != 0) {
                        echo "<td data-title='Vtas_Est'><a href='#' onClick='verVtaPMay(" . $dato_tienda['idTienda'] . "," . $mes_vent . "," . $anio_vent . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $ventas_prom . " Cajas</span></a></td>";
                    } else {
                        echo '<td data-title="Vtas_Est"><span>---</span></td>';
                    }
                    //Ventas Oficiales (Captura Venta)

                    $sql_vent = "SELECT ROUND(sum(cantidad),1) AS tot_vent,tipo
				FROM `venta_promedio` 
				WHERE idMarca IN (" . $filtro_mar . ") AND idTienda='" . $dato_tienda['idTienda'] . "'
				AND MONTH(fecha_inicio)='" . $mes_vent . "' AND YEAR(fecha_inicio)='" . $anio_vent . "' GROUP BY tipo";

                    $rs_vent = $manager->ejecutarConsulta($sql_vent);

                    $n_vent = mysqli_num_rows($rs_vent);

                    echo "<td data-title='Vtas_Ofic'>";

                    if ($n_vent >= 1) {
                        echo "<a href='#' onClick='verVtaOMay(" . $dato_tienda['idTienda'] . "," . $mes_vent . "," . $anio_vent . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>";
                        while ($dat_vent = mysqli_fetch_array($rs_vent)) {
                            echo $dat_vent['tot_vent'] . " " . strtolower($dat_vent['tipo']) . "<br/>";
                        }
                        echo "</span></a>";
                    } else {
                        echo "<span>---</span>";
                    }
                    echo " </td>";
                }


                if ($tipo_cons == 1 || $tipo_cons == 3) {

                    if ($im_tot != 0) {
                        echo "<td data-title='Marketing'> <a href='#' onClick='verIMMay(" . $dato_tienda['idTienda'] . "," . $fecha_c[0] . "," . $fecha_c[1] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $im_tot . " Regs</span></a></td>";
                    } else {
                        echo '<td data-title="Marketing"> <span>---</span></td>';
                    }
                }

                if ($tipo_cons == 1 || $tipo_cons == 4) {
                    if ($fotos_tot != 0) {
                        echo "<td data-title='Fotos'><a href='#' onClick='verFotMay(" . $dato_tienda['idTienda'] . "," . $fecha_c[0] . "," . $fecha_c[1] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $fotos_tot . " Regs</span></a></td>";
                    } else {
                        echo '<td data-title="Fotos"><span>---</span></td>';
                    }
                }
                if ($tipo_cons == 1 || $tipo_cons == 5) {
                    if ($inv_tot != 0) {
                        echo "<td data-title='Invent'><a href='#' onClick='verInvMay(" . $dato_tienda['idTienda'] . "," . $fecha_c[0] . "," . $fecha_c[1] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $inv_tot . " Cajas</span></a></td>";
                    } else {
                        echo '<td data-title="Invent"><span>---</span></td>';
                    }
                }
                //***********************************************************************************


            } else if ($noVisitadas == 1 && $dato_vis['tot_vis'] <= 0) {
                echo '
			<tr>
			<td ' . $visitada . ' data-title="No"><span>' . $dato_ruta['numeroEconomico'] . '</span></td>
			<td data-title="Cadena"><span>' . $dato_ruta['cadenaf'] . '</span></td>
			<td data-title="Grupo"><span>' . $dato_ruta['grupo'] . '</span></td>
			<td data-title="Tienda"><span>' . htmlentities($dato_ruta['sucursal']) . '</span></td>
			<td data-title="Ciudad"><span>' . htmlentities($dato_ruta['ciudadN']) . '</span></td>
			<td data-title="Estado"><span>' . htmlentities($dato_ruta['estadoMex']) . '</span></td>
			<td data-title="idProm"><span>' . $dato_tienda['idPromotor'] . '</span></td>
			';

            }
            echo '</tr>';
            usleep(1000);

        } //Si el mes es anterior se consultan resultados
        else {
            $sql_ruta = "SELECT mT.*,
			f.cadena AS cadenaf,
			e.nombre AS estadoMex,
			l.nombre AS ciudadN
			FROM maestroTiendas mT 
			LEFT JOIN tiendas_formatos f ON (mT.idFormato = f.idFormato)
			LEFT JOIN estados e ON (e.id = mT.idEstado)
			LEFT JOIN localidades l ON (mT.municipio=l.id)
			WHERE idTienda='" . $dato_tienda['idTienda'] . "'";

            $rs_ruta = $manager->ejecutarConsulta($sql_ruta);

            $dato_ruta = mysqli_fetch_array($rs_ruta);
            $sql_vis = "SELECT COUNT(idTiendasVisitadas) AS tot_vis FROM tiendasVisitadas
			WHERE fecha LIKE '%" . $fecha_c[0] . "-" . $fecha_c[1] . "%' AND idTienda='" . $dato_tienda['idTienda'] . "' AND tipo='E'";

            $rs_vis = $manager->ejecutarConsulta($sql_vis);

            $dato_vis = mysqli_fetch_array($rs_vis);

            if ($dato_vis['tot_vis'] > 0) {
                $visitada = "bgcolor='#58FA58'";
            } else {
                $visitada = "bgcolor='#FE2E2E'";
            }

            //*************** Si el reporte es completo ***********************/
            if ($noVisitadas == 0) {
                echo '
		<tr>
		<td ' . $visitada . ' data-title="No"><span>' . $dato_ruta['numeroEconomico'] . '</span></td>
		<td data-title="Cadena"><span>' . $dato_ruta['cadenaf'] . '</span></td>
		<td data-title="Grupo"><span>' . $dato_ruta['grupo'] . '</span></td>
		<td data-title="Tienda"><span>' . htmlentities($dato_ruta['sucursal']) . '</span></td>
		<td data-title="Ciudad"><span>' . htmlentities($dato_ruta['ciudadN']) . '</span></td>
		<td data-title="Estado"><span>' . htmlentities($dato_ruta['estadoMex']) . '</span></td>
		<td data-title="idProm"><span>' . $dato_tienda['idPromotor'] . '</span></td>
		';

//******************************REvision de datos de mayoreo_comp **************//
                $n_marc = count($id_marca);
                $ventas_prom = 0;
                $ventas_of = 0;
                $im_tot = 0;
                $fotos_tot = 0;
                $inv_tot = 0;
                if ($n_marc > 0) {
                    //*************Recorre las marcas seleccionadas
                    foreach ($id_marca as $marcas) {
                        //*************Si eligio una consulta completa

                        $query_mc = "SELECT * FROM mayoreo_comp
			WHERE mes='" . $mes . "' AND anio='" . $fecha_c[1] . "' AND idMarca='" . $marcas . "' AND idTienda='" . $dato_tienda['idTienda'] . "'";

                        $rs_mc = $manager->ejecutarConsulta($query_mc);

                        $dat_mc = mysqli_fetch_array($rs_mc);
                        $fotos_tot += $dat_mc['fotos'];
                        $im_tot += $dat_mc['inteligenciaM'];
                        $inv_tot += $dat_mc['inventarios'];


                        //***Consulta de Ventas
                        if ($tipo_cons == 2 || $tipo_cons == 1) {
                            $vtas_est = "SELECT sum(cajas) AS venta_est FROM surtidoMueble sm
					INNER JOIN Producto p ON (sm.idProducto=p.idProducto)
					WHERE (STR_TO_DATE(sm.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('" . $fecha_ini_v . "','%d-%m-%Y')
					AND STR_TO_DATE('" . $fecha_fin_v . "','%d-%m-%Y'))
					AND surtido='Si'  
					AND sm.idTienda='" . $dato_tienda['idTienda'] . "' AND p.idMarca='" . $marcas . "'";
                            $rs_vtaEs = $manager->ejecutarConsulta($vtas_est);

                            $dat_vtaEs = mysqli_fetch_array($rs_vtaEs);

                            if ($dat_vtaEs['venta_est'] > 0) {
                                $ventas_prom += $dat_vtaEs['venta_est'];
                            }

                        }


                    }

                    //*********************************** Totales de Datos ********************************
                    // Ventas
                    if ($tipo_cons == 1 || $tipo_cons == 2) {
                        //***Promedio (Surtido Mueble)
                        if ($ventas_prom != 0) {
                            echo "<td data-title='Vtas_Est'><a href='#' onClick='verVtaPMay(" . $dato_tienda['idTienda'] . "," . $mes_vent . "," . $anio_vent . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $ventas_prom . " Cajas</span></a></td>";
                        } else {
                            echo '<td data-title="Vtas_Est"><span>---</span></td>';
                        }
                        //Ventas Oficiales (Captura Venta)

                        $sql_vent = "SELECT ROUND(sum(cantidad),1) AS tot_vent,tipo
				FROM `venta_promedio` 
				WHERE idMarca IN (" . $filtro_mar . ") AND idTienda='" . $dato_tienda['idTienda'] . "'
				AND MONTH(fecha_inicio)='" . $mes_vent . "' AND YEAR(fecha_inicio)='" . $anio_vent . "' GROUP BY tipo";

                        $rs_vent = $manager->ejecutarConsulta($sql_vent);

                        $n_vent = mysqli_num_rows($rs_vent);

                        echo "<td data-title='Vtas_Ofic'>";

                        if ($n_vent >= 1) {
                            echo "<a href='#' onClick='verVtaOMay(" . $dato_tienda['idTienda'] . "," . $mes_vent . "," . $anio_vent . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>";
                            while ($dat_vent = mysqli_fetch_array($rs_vent)) {
                                echo $dat_vent['tot_vent'] . " " . strtolower($dat_vent['tipo']) . "<br/>";
                            }
                            echo "</span></a>";
                        } else {
                            echo "<span>---</span>";
                        }
                        echo " </td>";
                    }


                    if ($tipo_cons == 1 || $tipo_cons == 3) {

                        if ($im_tot != 0) {
                            echo "<td data-title='Marketing'> <a href='#' onClick='verIMMay(" . $dato_tienda['idTienda'] . "," . $fecha_c[0] . "," . $fecha_c[1] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $im_tot . " Regs</span></a></td>";
                        } else {
                            echo '<td data-title="Marketing"> <span>---</span></td>';
                        }
                    }

                    if ($tipo_cons == 1 || $tipo_cons == 4) {
                        if ($fotos_tot != 0) {
                            echo "<td data-title='Fotos'><a href='#' onClick='verFotMay(" . $dato_tienda['idTienda'] . "," . $fecha_c[0] . "," . $fecha_c[1] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $fotos_tot . " Regs</span></a></td>";
                        } else {
                            echo '<td data-title="Fotos"><span>---</span></td>';
                        }
                    }
                    if ($tipo_cons == 1 || $tipo_cons == 5) {
                        if ($inv_tot != 0) {
                            echo "<td data-title='Invent'><a href='#' onClick='verInvMay(" . $dato_tienda['idTienda'] . "," . $fecha_c[0] . "," . $fecha_c[1] . "," . json_encode($id_marca, JSON_PARTIAL_OUTPUT_ON_ERROR) . ")'><span>" . $inv_tot . " Cajas</span></a></td>";
                        } else {
                            echo '<td data-title="Invent"><span>---</span></td>';
                        }
                    }
                    //***********************************************************************************


                }

            } else if ($noVisitadas == 1 && $dato_vis['tot_vis'] <= 0) {
                echo '
			<tr>
			<td ' . $visitada . ' data-title="No"><span>' . $dato_ruta['numeroEconomico'] . '</span></td>
			<td data-title="Cadena"><span>' . $dato_ruta['cadenaf'] . '</span></td>
			<td data-title="Grupo"><span>' . $dato_ruta['grupo'] . '</span></td>
			<td data-title="Tienda"><span>' . htmlentities($dato_ruta['sucursal']) . '</span></td>
			<td data-title="Ciudad"><span>' . htmlentities($dato_ruta['ciudadN']) . '</span></td>
			<td data-title="Estado"><span>' . htmlentities($dato_ruta['estadoMex']) . '</span></td>
			<td data-title="idProm"><span>' . $dato_tienda['idPromotor'] . '</span></td>
			';

            }
            echo '</tr>';
            usleep(1000);

        }//****Final meses anteriores

    }/////****************Final While Tiendas


    echo '</table>
 </div>';// Cierra Tabla Principal
    ?>

    <?
} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



