module.exports = (app) => {
    const header = require('./headerController');

    // 1. 헤더에 필요한 정보 조회 API
    app.get('/header', header.getHeaderInfo);
};