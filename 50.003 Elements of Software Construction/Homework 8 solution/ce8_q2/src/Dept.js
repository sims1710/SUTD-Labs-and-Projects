import React, { useState, useEffect } from "react";

/**
 * Component for the Department input bar
 * the component consists of
 *   - a text input (dept code)
 *   - a submit button
 * @param {props} param0
 *    - code (state dept code)
 *    - onCodeChange (state dept code update)
 *    - onSubmitClick (submitButton click event handler)
 * @returns
 */
function NewDeptBar({ code, onCodeChange, onSubmitClick }) {
  return (
    <div>
      <input
        type="text"
        placeholder="department code"
        value={code}
        onChange={(e) => {
          onCodeChange(e.target.value);
        }}
      />
      <button onClick={onSubmitClick}>Submit</button>
    </div>
  );
}

// TASK 1, complete the following component so that the dept list can be rendered.
/**
 * Components for the Department list
 * @param {props} param0
 *    - a list of depts (depts state)
 * @returns
 */
function DeptList({ depts }) {
  return (
    <table>
      <tbody>
        {depts.map((dept, index) => (
          <tr key={index}>
            <td>{dept.code}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

function Dept() {
  const [code, setCode] = useState("");
  const [depts, setDepts] = useState([]);

  function handleSubmitClick() {
    submitNewDept();
  }

  /**
   * triggered when the submit button is clicked.
   * submit a new dept by calling the API
   * then set the depts state, which will
   * render the dept table
   */
  async function submitNewDept() {
    const response = await fetch(`http://localhost:3000/dept/submit`, {
      method: "POST",
      body: `code=${code}`,
      headers: {
        "Content-type": "application/x-www-form-urlencoded",
      },
    });
    const text = await response.text();
    const json = JSON.parse(text);
    setDepts(json);
  }

  /**
   * triggered when the component did mount.
   * submit to API to query all the depts
   * then set the depts state, which will
   * render the dept table
   */
  async function initDepts() {
    const response = await fetch(`http://localhost:3000/dept/all`);
    const text = await response.text();
    const json = JSON.parse(text);
    setDepts(json);
  }

  useEffect(() => {
    initDepts();
  }, []);

  return (
    <div>
      <NewDeptBar
        code={code}
        onCodeChange={setCode}
        onSubmitClick={handleSubmitClick}
      />
      <DeptList depts={depts} />
    </div>
  );
}

export default Dept;
