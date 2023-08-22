module.exports = (app) => {
    const work = require('./workController');

    // 1. 전체 작업 조회 API
    app.get('/work/:teamId', work.getWorks);

    // 2. 작업 등록 API
    app.post('/work/:teamId',work.postWork);

    // 3. 특정 작업 조회 API
    app.get('/work/detail/:workId', work.getWorkById);

    //4. 특정 작업 삭제 API
    app.delete('/work/:workId', work.deleteWork);

    //5. 특정 작업 전체 수정 API
    app.put('/work/:workId', work.putWork);

    //6. 특정 작업 하나 수정 API
    app.patch('/work/:workId/:updateValue', work.patchWork);
};