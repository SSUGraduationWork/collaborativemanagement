const contribution = require("./contributionController");
module.exports = (app) => {

    // 1. 전체 기여도 조회 API
    app.get('/contribution/:teamId', contribution.getContributions);

    // 2. 특정 유저의 작업 조회 API
    app.get
};