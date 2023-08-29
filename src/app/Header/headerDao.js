const headerDao = {
    selectUser : async(connection, userId) => {
        const selectUserQuery = `
            SELECT user_name, student_number, department, picture_url
            FROM Members
            WHERE id = ?;
        `;

        const [User] = await connection.query(selectUserQuery, userId);
        return User;
    },
    selectTeamName: async (connection, teamId) => {
        const selectTeamNameQuery = `
            SELECT CONCAT(project_name,' ',team_name) AS team_name 
            FROM Teams T 
            LEFT JOIN Projects P 
            ON T.project_id = P.project_id
            WHERE team_id = ?;
        `;
        const [teamName] = await connection.query(selectTeamNameQuery, teamId);
        return teamName;
    },
}

module.exports = headerDao;