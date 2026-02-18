import { useNavigate } from "react-router-dom";

const JobTable = ({ jobs = [] }) => {
  const navigate = useNavigate();

  return (
    <div className="bg-white shadow rounded-lg">
      <table className="w-full">
        <thead className="bg-gray-100">
          <tr>
            <th>JobId</th>
            <th>JobTitle</th>
            <th>JobStatus</th>
            <th>View</th>
          </tr>
        </thead>

        <tbody>
          {jobs.map((t) => (
            <tr key={t.jobId} className="border-t text-center">
              <td className="p-3">{t.jobId}</td>
              <td>{t.title}</td>
              <td>{t.status}</td>
              <td>
                <button
                  className="text-blue-600"
                  onClick={() => navigate(`/job/${t.jobId}`)}>
                  View
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default JobTable;