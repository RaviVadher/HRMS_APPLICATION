import { useState } from "react";
import { searchEmployees } from "../orgAPI";

const SearchEmployee = ({ onSelect }) => {

  const [query,setQuery] = useState("");
  const [results,setResults] = useState([]);

  const handleSearch = async (e) => {
    const val = e.target.value;
    setQuery(val);
    if(val.length < 2) return;
    const res = await searchEmployees(val);
    setResults(res);
  };

  return (
    <div className="mb-6">

      <input
        placeholder="Search employee..."
        value={query}
        onChange={handleSearch}
        className="w-full px-4 py-2 border border-gray-300 rounded-lg 
                   focus:outline-none focus:ring-2 focus:ring-blue-500 
                   focus:border-blue-500 transition" />

      {results.length > 0 && (
        <div className="border rounded mt-2 bg-white">
          {results.map(emp => (
            <div
              key={emp.id}
              className="p-2 hover:bg-gray-100 cursor-pointer"
              onClick={() => {
                onSelect(emp.id);
                setResults([]);
              }} >
              {emp.name} — {emp.designation}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SearchEmployee;