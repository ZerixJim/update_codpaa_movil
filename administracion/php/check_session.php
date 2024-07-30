<?php
session_start();


if (isset($_SESSION['idUser'])){

    echo json_encode(array('session'=> true));
}else{

    echo json_encode(array('session'=> false));
}





