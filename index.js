const app = require('./config/express');
const port = 3000;

app.listen(port, () => {
    console.log(`${port}에서 서버 가동`);
});