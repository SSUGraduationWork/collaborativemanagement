SELECT * from (Members M inner join co_work.Projects P on M.id = P.professor_id)
INNER JOIN Teams T on P.professor_id = T.project_id