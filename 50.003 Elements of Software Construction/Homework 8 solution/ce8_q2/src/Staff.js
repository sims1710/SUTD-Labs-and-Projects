import React, { useState, useEffect } from "react";

/**
 * Component for the Staff input bar
 * the component consists of
 *   - a text box (name of the staff)
 *   - a dropdown (dept code of the staff)
 *   - a submit button
 * @param {props} param0
 *    - name (state name)
 *    - code (state dept code)
 *    - depts (state the list of all depts)
 *    - onNameChange (state name update)
 *    - onCodeChange (state dept code update)
 *    - onSubmitClick (submitButton click event handler)
 * @returns
 */
function NewStaffBar({ name, code, depts, onNameChange, onCodeChange, onSubmitClick }) {
  let rows = [];
  for (let i in depts) {
    if (depts[i].code === code) {
      rows.push(<option value={depts[i].code} selected>{depts[i].code}</option>);
    } else {
      rows.push(<option value={depts[i].code}>{depts[i].code}</option>);
    }
  }
  return (
    <div>
      <input
        type="text"
        placeholder="name"
        value={name}
        onChange={(e) => {
          onNameChange(e.target.value);
        }}
      />
      <select onChange={(e) => { onCodeChange(e.target.value) }}>
        {rows}
      </select>
      <button onClick={onSubmitClick}>Submit</button>
    </div>
  );
}

/**
 * Componenet for the staff lists
 * render the list of staffs (from the state) into a table
 * @param {props} param0
 *   - staffs (a list of all staffs state)
 * @returns
 */
function StaffList({ staffs }) {
  return (
    <table>
      <tbody>
        <tr><th>Staff Name</th><th>Department</th></tr>
        {staffs.map((staff, index) => (
          <tr key={index}>
            <td>{staff.name}</td>
            <td>{staff.code}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

function Staff() {
  const [name, setName] = useState("");
  const [code, setCode] = useState("");
  const [staffs, setStaffs] = useState([]);
  const [depts, setDepts] = useState([]);

  function handleSubmitClick() {
    submitNewStaff();
  }

  /**
   * triggered when the submit button is clicked.
   * submit a new staff by calling the API
   * then set the staffs state, which will
   * render the staff table
   */
  async function submitNewStaff() {
    const response = await fetch(`http://localhost:3000/staff/submit`, {
      method: "POST",
      body: `name=${name}&code=${code}`,
      headers: {
        "Content-type": "application/x-www-form-urlencoded",
      },
    });
    const text = await response.text();
    const json = JSON.parse(text);
    setStaffs(json);
  }

  useEffect(() => {
    initStaffs();
    initDepts(); // Load the list of dept codes when the component is mounted
  }, []);

  /**
   * triggered when the component did mount.
   * submit to API to query all the staffs
   * then set the staffs state, which will
   * render the staff table
   */
  async function initStaffs() {
    const response = await fetch(`http://localhost:3000/staff/all`);
    const text = await response.text();
    const json = JSON.parse(text);
    setStaffs(json);
  }

  /**
   * triggered when the component did mount.
   * submit to API to query all the depts
   * then set the depts state, which will
   * render the dropdown list of dept codes.
   */
  async function initDepts() {
    const response = await fetch(`http://localhost:3000/dept/all`);
    const text = await response.text();
    const json = JSON.parse(text);
    setDepts(json);
  }

  return (
    <div>
      <NewStaffBar
        name={name}
        code={code}
        depts={depts}
        onCodeChange={setCode}
        onNameChange={setName}
        onSubmitClick={handleSubmitClick}
      />
      <StaffList staffs={staffs} />
    </div>
  );
}

export default Staff;
