UPDATE step2.states
SET name = 'High'
WHERE name LIKE '%Above%';

UPDATE step2.states
SET name = 'Low'
WHERE name LIKE '%Below%';