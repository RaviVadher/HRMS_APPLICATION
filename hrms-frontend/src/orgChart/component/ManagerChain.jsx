import ArrowRightAltIcon from '@mui/icons-material/ArrowRightAlt';
const ManagerChain = ({ managers, onSelect }) => {

  return (
    <div className="">
      <b>MANAGER CHAIN</b>
      <hr className="my-2 block" />
      <div className="flex gap-3 mt-4 mb-6 flex-wrap justify-center">
      {managers.length===0 && (
        <div className="cursor-not-allowed items-center px-4 py-2 border border-black rounded hover:bg-gray-300">
              <i>TOP LEVEL</i>
        </div>
      )}
        {managers.map((m, idx) => (
          <>
            <div
              key={m.id}
              onClick={() => onSelect(m.id)}
              className="cursor-pointer items-center px-4 py-2 border border-black rounded hover:bg-gray-300">
              {m.name}
            </div>
            {idx < managers.length - 1 && (<span className="text-black-900 bold mt-2 mx-4"><ArrowRightAltIcon/></span>)}
          </>
        ))}
      </div>
    </div>
  );
};

export default ManagerChain;
