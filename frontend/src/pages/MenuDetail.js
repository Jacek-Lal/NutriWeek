import { useParams } from "react-router-dom";

function MenuDetail() {
  const { id } = useParams();
  console.log(id);
  return <h1 className="text-white">Menu detail for ID: {id}</h1>;
}
export default MenuDetail;
