const contributionDao = {
    selectContributions : async(connection, teamId) => {
        const selectContributionsQuery = `
            SELECT M.id AS user_id, CONCAT(M.student_number, ' ', M.user_name) AS team_member, C.contribution
            FROM Members M
                LEFT JOIN Contributions C
                    ON M.id = C.user_id
            WHERE C.team_id = ?;
        `;

        const [contributionRows] = await connection.query(selectContributionsQuery, teamId);
        return contributionRows;
    },
    selectWorkProgress : async(connection, teamId) => {
        const selectWorkProgressQuery = `
            SELECT count(*) AS totalWorks, 
                   count(CASE WHEN status = 1 THEN 1 END) AS notStarted, 
                   count(CASE WHEN status = 3 and status = 2 THEN 1 END) AS inProgress, 
                   count(CASE WHEN status = 4 THEN 1 END) AS done 
            FROM Works 
            WHERE team_id = ?;
        `
        const [workProgress] = await connection.query(selectWorkProgressQuery, teamId);
        return workProgress;
    }
}

module.exports = contributionDao;