-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 13-Nov-2017 às 18:52
-- Versão do servidor: 10.1.26-MariaDB
-- PHP Version: 7.1.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `banco_desafio_backend`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `tb_customer_account`
--

CREATE TABLE `tb_customer_account` (
  `id_customer` int(11) NOT NULL,
  `cpf_cnpj` varchar(14) NOT NULL,
  `nm_customer` varchar(45) NOT NULL,
  `is_active` char(1) NOT NULL,
  `vl_total` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `tb_customer_account`
--

INSERT INTO `tb_customer_account` (`id_customer`, `cpf_cnpj`, `nm_customer`, `is_active`, `vl_total`) VALUES
(100, '11111111111', 'Teste4', '0', '750.00'),
(1000, '22222222222', 'Teste', '1', '800.00'),
(1744, '33333333333', 'Teste 5', '1', '999.00'),
(1999, '42476738824', 'Marcos Tesolin Munhoz', '1', '999.88'),
(2000, '44444444444', 'Teste2', '0', '1000.00'),
(2222, '11111111111111', 'Teste cnpj', '1', '888.00'),
(2500, '55555555555', 'Teste3', '1', '900.00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_customer_account`
--
ALTER TABLE `tb_customer_account`
  ADD PRIMARY KEY (`id_customer`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
