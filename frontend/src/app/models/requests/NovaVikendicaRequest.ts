import { vikendica } from "../vikendica";

export class NovaVikendicaRequest{
  vikendica: vikendica = new vikendica();
  slike: string[] = [];
  cenovnikLeto: number = 0;
  cenovnikZima: number = 0;
}
