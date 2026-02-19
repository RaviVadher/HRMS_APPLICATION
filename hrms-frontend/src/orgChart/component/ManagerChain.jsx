const ManagerChain = ({ managers, onSelect }) => {

  if(!managers?.length) return null;
  return (
    <>
    <b>MANAGER CHAIN</b>
    <div className="flex gap-3 mb-6 flex-wrap">

      {managers.map(m => (
        <div
          key={m.id}
          onClick={() => onSelect(m.id)}
          className="cursor-pointer bg-gray-200 px-4 py-2 rounded hover:bg-gray-300">
          {m.name} <span>----</span>
        </div>
      ))}
    </div>
    </>
  );
};

export default ManagerChain;