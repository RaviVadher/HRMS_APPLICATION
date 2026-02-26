const DirectReports = ({ reports, onSelect }) => {

  if (!reports?.length) return null;

  return (
    <div>
        <b>Direct Reports</b>
        <hr />
      <div className="flex gap-3 mt-3 mb-6 flex-wrap">
        {reports.map(r => (
          <div
            key={r.id}
            onClick={() => onSelect(r.id)}
            className="cursor-pointer px-4 py-2 border border-black rounded hover:bg-gray-300">
            <p className="font-medium">{r.name}</p>
            <p className="text-sm text-gray-500">{r.designation}</p>
          </div>
        ))}

      </div>
    </div>
  );
};

export default DirectReports;