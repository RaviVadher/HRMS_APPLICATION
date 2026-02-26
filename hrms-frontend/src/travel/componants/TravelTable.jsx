import { useNavigate } from "react-router-dom";

const TravelTable = ({ travels = [] }) => {
  const navigate = useNavigate();
  console.log(travels)

  return (
    <div className="bg-white shadow rounded-lg">
      <table className="w-full">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3">Destination</th>
            <th>Start</th>
            <th>End</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {travels.length === 0 ? (
            <tr><td>No Travel Found</td></tr>
          ) :
            (travels.map((t) => (
              <tr key={t.travelId} className="border-t text-center">
                <td className="p-3">{t.destination}</td>
                <td>{t.startDate}</td>
                <td>{t.endDate}</td>
                <td>
                  <button
                    className="text-blue-600"
                    onClick={() => navigate(`/travel/${t.travelId}`)}
                  >
                    View
                  </button>
                </td>
              </tr>
            )))}
        </tbody>
      </table>
    </div>
  );
};

export default TravelTable;