const workDao = {

    selectWorks: async (connection, teamId) => {
        const selectWorksQuery = `
            SELECT work_id, work_name, GROUP_CONCAT(A.user_info SEPARATOR ';') AS worker, GROUP_CONCAT(A.picture_url SEPARATOR ';') AS picture_url, end_date, importance, status
            FROM (SELECT Works.work_id, work_name, end_date, importance, status, Members.picture_url, CONCAT(Members.student_number, ' ', Members.user_name) AS user_info
                  FROM Works
                           LEFT JOIN Workers
                                     ON Workers.work_id = Works.work_id
                           LEFT JOIN Members
                                     ON Members.id = Workers.user_id
                  WHERE Works.team_id = ?) AS A
            GROUP BY work_id;   
        `;
        const [workRows] = await connection.query(selectWorksQuery, teamId);
        return workRows;
    },

    selectTeamMembers: async (connection, teamId) => {
        const selectTeamMembersQuery = `
            SELECT user_id, CONCAT(student_number, ' ', user_name) AS user_name, picture_url 
            FROM Team_members T 
            JOIN Members M 
            ON T.user_id = M.id 
            WHERE team_id = 1;
        `;
        const [teamMembers] = await connection.query(selectTeamMembersQuery, teamId);
        return teamMembers;
    },

    selectWorkById: async (connection, workId) => {
        const selectWorkByIdQuery =`
            SELECT work_id, work_name, GROUP_CONCAT(A.user_info SEPARATOR ';') AS worker, end_date, importance, status
            FROM (SELECT Works.work_id, work_name, end_date, importance, status, CONCAT(Members.student_number, ' ', Members.user_name) AS user_info
                  FROM Works
                           LEFT JOIN Workers
                                     ON Workers.work_id = Works.work_id
                           LEFT JOIN Members
                                     ON Members.id = Workers.user_id
                  WHERE Works.work_id = ?) AS A
            GROUP BY work_id;
        `;

        const [workRows] = await connection.query(selectWorkByIdQuery, workId);
        return workRows;
    },

    selectBoardsAboutWork: async (connection, workId) => {
        const selectBoardsAboutWorkQuery = `
            SELECT board_id, CONCAT(M.student_number, ' ', M.user_name) AS writer, title
            FROM Boards
                     LEFT JOIN Members M
                               ON Boards.user_id = M.id
            WHERE work_id = ?;
        `;

        const [boardRows] = await connection.query(
            selectBoardsAboutWorkQuery,
            workId
        );
        return {boards : boardRows};
    },

    insertWork: async (connection, insertWorkParams) => {

        const insertWorkQuery = `
            INSERT INTO Works(team_id, work_name, end_date, importance, status, worker_number)
            VALUES (?, ?, ?, ?, ?, ?);
        `;

        const insertWorkInfoRow = await connection.query(
            insertWorkQuery,
            insertWorkParams
        );

        return insertWorkInfoRow;
    },

    insertWorkers : async(connection, workId, worker_arr) => {
        const insertWorkersQuery = `
            INSERT INTO Workers(work_id, user_id)
            VALUES (?,?);
        `;

        try{
            for (const userId of worker_arr) {
                await connection.query(insertWorkersQuery, [workId, userId]);
            }
            return true
        } catch(err) {
            console.log(err)
            return false;
        }
    },

    deleteWork: async (connection, workId) => {
        const deleteWorkQuery = `
            DELETE FROM Works WHERE work_id = ?;
        `

        const deleteWorkInfoRow = await connection.query(
            deleteWorkQuery,
            workId
        );

        return deleteWorkInfoRow;
    },

    updateWork: async (connection, workId, updateWorkParams) => {

        const updateWorkQuery = `
            UPDATE Works SET work_name = ?, end_date = ?, importance = ?, status = ?, worker_number = ?
            WHERE work_id = ${workId};   
        `;

        const updateWorkInfoRow = await connection.query(
            updateWorkQuery,
            updateWorkParams
        );

        return updateWorkInfoRow;
    },

    deleteWorkers: async (connection, workId) => {
        const deleteWorkersQuery = `
            DELETE FROM Workers WHERE work_id = ?
        `

        const deleteWorkInfoRow = await connection.query(
            deleteWorkersQuery,
            workId
        );

        return deleteWorkInfoRow;
    }
}
module.exports = workDao;