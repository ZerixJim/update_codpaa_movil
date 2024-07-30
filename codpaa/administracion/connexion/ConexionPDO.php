<?php
/**
 * Created by PhpStorm.
 * User: Gustavo Ibarra
 * Date: 10/04/2019
 * Time: 12:13
 */


require_once 'configuraciones.php';



class ConexionPDO
{


    private static $db = null;

    private static $pdo;


    final private function __construct()
    {

        try{
            self::getDB();
        }catch (PDOException $e){

        }

    }

    public function getDB(){

        if (self::$pdo == null){

            self::$pdo = new PDO('mysql:dbname='.config::getBBDDName().';host='.config::getBBDDServer().';',
                config::getBBDDUser(), config::getBBDDPwd(), array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8"));

            self::$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        }

        return self::$pdo;


    }


    public static function getInstance(){

        if (self::$db == null){

            self::$db = new self();


        }
        return self::$db;

    }


    final protected function __clone(){}

    function __destruct(){
        self::$pdo = null;
    }


}