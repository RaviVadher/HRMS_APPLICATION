const DirectReports = ({ reports, onSelect }) => {

  if (!reports?.length) return null;

  return (
    <div>
      <h3 className="font-semibold mb-3">
        Direct Reports
      </h3>

      <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
        {reports.map(r => (
          <div
            key={r.id}
            onClick={() => onSelect(r.id)}
            className="p-4 border rounded cursor-pointer hover:bg-gray-100 text-center">
            <p className="font-medium">{r.name}</p>
            <p className="text-sm text-gray-500">{r.designation}</p>
          </div>
        ))}

      </div>
    </div>
  );
};

export default DirectReports;