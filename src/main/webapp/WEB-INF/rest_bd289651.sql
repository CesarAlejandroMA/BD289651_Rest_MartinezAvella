-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-06-2025 a las 01:04:50
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `rest_bd289651`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `espacio`
--

CREATE TABLE `espacio` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `espacio`
--

INSERT INTO `espacio` (`id`, `nombre`, `descripcion`) VALUES
(1, 'A11', 'Primer Salon'),
(2, 'A102', 'Segundo Salon'),
(3, 'B101', 'Tercer Salon'),
(5, 'B103', 'Quinto Salon'),
(6, 'C101', 'Otro salon'),
(10, 'A101', 'Primer salon'),
(12, 'asd123', 'sfdsfdfds'),
(13, 'asd321', 'vvsdvs'),
(14, 'as', '12'),
(15, 'A102102', 'Descripcion del espacio');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hueco`
--

CREATE TABLE `hueco` (
  `id` int(11) NOT NULL,
  `espacio_id` int(11) NOT NULL,
  `inicio` datetime NOT NULL,
  `fin` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hueco`
--

INSERT INTO `hueco` (`id`, `espacio_id`, `inicio`, `fin`) VALUES
(1, 6, '2025-06-26 21:18:00', '2025-06-26 23:18:00'),
(2, 6, '2025-06-26 10:39:00', '2025-06-26 11:40:00'),
(3, 1, '2025-06-26 21:43:00', '2025-06-26 21:44:00'),
(4, 6, '2025-06-26 11:40:00', '2025-06-26 12:44:00'),
(5, 6, '2025-06-26 23:45:00', '2025-06-26 23:46:00'),
(6, 6, '2025-06-10 11:48:00', '2025-06-10 11:49:00'),
(7, 1, '2025-06-21 12:53:00', '2025-06-21 12:54:00'),
(11, 1, '2025-06-19 17:04:00', '2025-06-19 17:10:00'),
(12, 1, '2025-06-19 17:21:00', '2025-06-19 17:22:00');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `espacio`
--
ALTER TABLE `espacio`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `hueco`
--
ALTER TABLE `hueco`
  ADD PRIMARY KEY (`id`),
  ADD KEY `espacio_id` (`espacio_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `espacio`
--
ALTER TABLE `espacio`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `hueco`
--
ALTER TABLE `hueco`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `hueco`
--
ALTER TABLE `hueco`
  ADD CONSTRAINT `hueco_ibfk_1` FOREIGN KEY (`espacio_id`) REFERENCES `espacio` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
