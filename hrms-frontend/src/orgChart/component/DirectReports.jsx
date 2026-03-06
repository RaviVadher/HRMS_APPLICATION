import ArrowDownwardIcon from '@mui/icons-material/ArrowDownward';
const DirectReports = ({ reports, onSelect }) => {

  if (!reports?.length) return null;

  return (
    <div className="">
        <b>DIRECT REPORTS</b>
        <hr className="w-full my-2"/>

      <div className="flex gap-6 mt-3 mb-6 flex-wrap justify-center">
        {reports.map(r => (
          <div  key={r.id} className="flex flex-col items-center">
          <ArrowDownwardIcon className="mb-2"/>
          <div
            onClick={() => onSelect(r.id)}
            className="cursor-pointer px-4 py-2 border border-black rounded hover:bg-gray-300">
            <p className="font-medium">{r.name}</p>
            <p className="text-sm text-gray-500">{r.designation}</p>
          </div>
          </div>
        ))}

      </div>
    </div>
  );
};

export default DirectReports;