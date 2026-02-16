const EmployeeCard = ({ employee, highlight }) => {
  return (
    <div className={`p-6 rounded shadow mb-6 text-center 
      ${highlight ? "bg-blue-100" : "bg-white"}`}>
      <h3 className="font-semibold text-lg">{employee.name}</h3>
      <p className="text-gray-600">{employee.designation}</p>
      <p className="text-sm text-gray-500">{employee.department}</p>
    </div>
  );
};

export default EmployeeCard;