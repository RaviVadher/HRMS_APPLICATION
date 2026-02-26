const ManagerChain = ({ managers, onSelect }) => {

  if (!managers?.length) return null;
  return (
    <>
      <b>MANAGER CHAIN</b>
      <hr />
      <div className="flex gap-3 mt-4 mb-6 flex-wrap">
        {managers.map((m, idx) => (
          <>
            <div
              key={m.id}
              onClick={() => onSelect(m.id)}
              className="cursor-pointer px-4 py-2 border border-black rounded hover:bg-gray-300">
              {m.name}
            </div>
            {idx < managers.length - 1 && (<span className="text-black-900 bold mt-2 mx-4">{'--->'}</span>)}
          </>
        ))}
      </div>
    </>
  );
};

export default ManagerChain;
