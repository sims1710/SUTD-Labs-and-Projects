const express = require('express');
const deptmodel = require('../models/dept.js');
const staffmodel = require('../models/staff.js');
var router = express.Router();

// Add a department
router.post('/add/:code', async function(req, res, next) {
  const code = req.params.code;
  const department = new deptmodel({ code });

  try {
    await department.save();
    res.send('Department added successfully');
  } catch (error) {
    console.error('Error adding department:', error);
    res.status(500).send('Error adding department');
  }
});

// Get all departments
router.get('/all/', async function(req, res, next) {
  try {
    const departments = await deptmodel.find();
    res.json(departments);
  } catch (error) {
    console.error('Error getting departments:', error);
    res.status(500).send('Error getting departments');
  }
});

// Get all departments with staff
router.get('/all/withstaff/', async function(req, res, next) {
  try {
    const departments = await deptmodel.find().populate('staffs');
    res.json(departments);
  } catch (error) {
    console.error('Error getting departments with staff:', error);
    res.status(500).send('Error getting departments with staff');
  }
});

module.exports = router;