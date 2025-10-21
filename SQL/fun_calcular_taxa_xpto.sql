USE xpto_db;

DROP FUNCTION IF EXISTS fn_calcular_taxa_xpto;

DELIMITER $$

CREATE FUNCTION fn_calcular_taxa_xpto(p_quantidade_movimentacoes INT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
NO SQL
BEGIN
    DECLARE v_taxa DECIMAL(10,2);
  
    IF p_quantidade_movimentacoes <= 10 THEN
        SET v_taxa = p_quantidade_movimentacoes * 1.00;
    ELSEIF p_quantidade_movimentacoes <= 20 THEN
        SET v_taxa = (10 * 1.00) + ((p_quantidade_movimentacoes - 10) * 0.75);
        
    ELSE
        SET v_taxa = (10 * 1.00) + (10 * 0.75) + ((p_quantidade_movimentacoes - 20) * 0.50);
    END IF;
    
    RETURN v_taxa;
END$$

DELIMITER ;