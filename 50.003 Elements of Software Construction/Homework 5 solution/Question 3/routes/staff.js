const express = require('express');
const staffmodel = require('../models/staff.js');
const deptmodel = require('../models/dept.js');
var router = express.Router();

// Add a staff
router.post('/add/:id/:name/:code', async function(req, res, next) {
  const id = req.params.id;
  const name = req.params.name;
  const code = req.params.code;

  try {
    const department = await deptmodel.findOne({ code });

    if (!department) {
      res.status(404).send('Department not found');
      return;
    }

    const staff = new staffmodel({ id, name, department });
    await staff.save();
    res.send('Staff added successfully');
  } catch (error) {
    console.error('Error adding staff:', error);
    res.status(500).send('Error adding staff');
  }
});

// Get all staff
router.get('/all/', async function(req, res, next) {
  try {
    const staffs = await staffmodel.find();
    res.json(staffs);
  } catch (error) {
    console.error('Error getting staff:', error);
    res.status(500).send('Error getting staff');
  }
});

// Get staff by department code
router.get('/bydept/:code', async function(req, res, next) {
  const code = req.params.code;

  try {
    const department = await deptmodel.findOne({ code });

    if (!department) {
      res.status(404).send('Department not found');
      return;
    }

    const staffs = await staffmodel.find({ department: department._id });
    res.json(staffs);
  } catch (error) {
    console.error('Error getting staff by department code:', error);
    res.status(500).send('Error getting staff by department code');
  }
});

// Get staff by ID
router.get('/byid/:id', async function(req, res, next) {
  const id = req.params.id;

  try {
    const staff = await staffmodel.findOne({ id });

    if (!staff) {
      res.status(404).send('Staff not found');
      return;
    }

    res.json(staff);
  } catch (error) {
    console.error('Error getting staff by ID:', error);
    res.status(500).send('Error getting staff by ID');
  }
});

module.exports = router;