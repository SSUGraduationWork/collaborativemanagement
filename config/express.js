const express = require('express');
const dotenv = require('dotenv');
const compression = require('compression');
const methodOverride = require('method-override');
const cors = require('cors');

dotenv.config();

const app = express();

app.use(compression());

app.use(express.json());
app.use(express.urlencoded({extended:true}));

app.use(methodOverride());
app.use(cors());

require('../src/app/Work/workRoute')(app);
//require('../src/app/Contribution/contributionRoute')(app);
//require('../src/app/Header/headerRoute')(app);

module.exports = app;